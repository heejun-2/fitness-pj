package org.example.fitnesspj.application.workout;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.fitnesspj.api.workout.dto.WorkoutCreateRequest;
import org.example.fitnesspj.api.workout.dto.WorkoutDailyGroupResponse;
import org.example.fitnesspj.api.workout.dto.WorkoutResponse;
import org.example.fitnesspj.domain.exercise.Exercise;
import org.example.fitnesspj.domain.exercise.ExerciseRepository;
import org.example.fitnesspj.domain.record.SetRecord;
import org.example.fitnesspj.domain.user.User;
import org.example.fitnesspj.domain.user.UserRepository;
import org.example.fitnesspj.domain.workout.Workout;
import org.example.fitnesspj.domain.workout.WorkoutRepository;
import org.example.fitnesspj.global.exception.BusinessException;
import org.example.fitnesspj.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;

    public Long createWorkout(Long userId, WorkoutCreateRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Workout workout = Workout.create(user, request.getWorkoutDate(), request.getMemo());

        for (WorkoutCreateRequest.SetCreateRequest s : request.getSets()) {
            Exercise exercise = exerciseRepository.findById(s.getExerciseId())
                    .orElseThrow(() -> new IllegalArgumentException("운동 종목을 찾을 수 없습니다. id=" + s.getExerciseId()));

            SetRecord record = SetRecord.create(exercise, s.getWeight(), s.getReps(), s.getSetOrder());
            workout.addSet(record); // 연관관계 편의 메서드 (cascade로 같이 저장됨)
        }

        Workout saved = workoutRepository.save(workout);

        return saved.getId();
    }

    @Transactional(readOnly = true)
    public List<WorkoutResponse> getWorkoutsByDate(Long userId, LocalDate date) {

        List<Workout> workouts = workoutRepository.findAllByUserIdAndDateFetchJoin(userId, date);

        return toWorkoutResponses(workouts);
    }

    // 기간 조회 결과 -> 날짜별X
    @Transactional(readOnly = true)
    public List<WorkoutResponse> getWorkoutsByPeriod(@NonNull Long userId, @NonNull LocalDate from, @NonNull LocalDate to) {

        List<Workout> workouts = workoutRepository.findAllByUserIdAndDateBetweenFetchJoin(userId, from, to);

        return toWorkoutResponses(workouts);
    }

    // 기간 조회 결과 -> 날짜별로 반환
    @Transactional(readOnly = true)
    public List<WorkoutDailyGroupResponse> getWorkoutsGroupedByDate(Long userId, LocalDate from, LocalDate to) {

        List<Workout> workouts = workoutRepository.findAllByUserIdAndDateBetweenFetchJoin(userId, from, to);

        // 날짜별 그룹핑용 Map
        Map<LocalDate, List<WorkoutResponse>> grouped = new LinkedHashMap<>();

        for (Workout w : workouts) {
            LocalDate date = w.getWorkoutDate();

            // 날짜 키 없으면 생성
            grouped.computeIfAbsent(date, d -> new ArrayList<>());

            grouped.get(date).add(toWorkoutResponse(w));
        }

        // Map → List<Response>
        List<WorkoutDailyGroupResponse> result = new ArrayList<>();
        for (Map.Entry<LocalDate, List<WorkoutResponse>> entry : grouped.entrySet()) {
            result.add(new WorkoutDailyGroupResponse(entry.getKey(), entry.getValue()));
        }

        return result;
    }

    // 단건 상세조회
    @Transactional(readOnly = true)
    public WorkoutResponse getWorkoutDetail(Long userId, Long workoutId){

        Workout workout = workoutRepository.findDetailByIdAndUserIdFetchJoin(workoutId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        return toWorkoutResponse(workout);
    }


    // 삭제
    public void deleteWorkout(Long userId, Long workoutId){
        long deleted = workoutRepository.deleteByIdAndUserId(workoutId, userId);

        if(deleted == 0){
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
    }

    private List<WorkoutResponse> toWorkoutResponses(List<Workout> workouts) {
        List<WorkoutResponse> responses = new ArrayList<>();

        for (Workout w : workouts) {

            List<SetRecord> setRecords = w.getSetRecords();
            setRecords.sort(Comparator.comparingInt(SetRecord::getSetOrder));

            List<WorkoutResponse.SetResponse> setResponses = new ArrayList<>();

            for (SetRecord sr : setRecords) {
                setResponses.add(
                        WorkoutResponse.SetResponse.builder()
                                .setRecordId(sr.getId())
                                .setOrder(sr.getSetOrder())
                                .weight(sr.getWeight())
                                .reps(sr.getReps())
                                .exerciseId(sr.getExercise().getId())
                                .exerciseName(sr.getExercise().getName())
                                .exerciseCategory(sr.getExercise().getCategory())
                                .build()
                );
            }

            responses.add(
                    WorkoutResponse.builder()
                            .workoutId(w.getId())
                            .workoutDate(w.getWorkoutDate())
                            .memo(w.getMemo())
                            .sets(setResponses)
                            .build()
            );
        }

        return responses;
    }

    private WorkoutResponse toWorkoutResponse(Workout w) {

        List<SetRecord> setRecords = new ArrayList<>(w.getSetRecords());
        setRecords.sort(Comparator.comparingInt(SetRecord::getSetOrder));

        List<WorkoutResponse.SetResponse> setResponses = new ArrayList<>();
        for (SetRecord sr : setRecords) {
            setResponses.add(
                    WorkoutResponse.SetResponse.builder()
                            .setRecordId(sr.getId())
                            .setOrder(sr.getSetOrder())
                            .weight(sr.getWeight())
                            .reps(sr.getReps())
                            .exerciseId(sr.getExercise().getId())
                            .exerciseName(sr.getExercise().getName())
                            .exerciseCategory(sr.getExercise().getCategory())
                            .build()
            );
        }

        return WorkoutResponse.builder()
                .workoutId(w.getId())
                .workoutDate(w.getWorkoutDate())
                .memo(w.getMemo())
                .sets(setResponses)
                .build();
    }
}
