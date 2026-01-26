package org.example.fitnesspj.application.workout;

import lombok.RequiredArgsConstructor;
import org.example.fitnesspj.api.workout.dto.WorkoutCreateRequest;
import org.example.fitnesspj.api.workout.dto.WorkoutResponse;
import org.example.fitnesspj.domain.exercise.Exercise;
import org.example.fitnesspj.domain.exercise.ExerciseRepository;
import org.example.fitnesspj.domain.record.SetRecord;
import org.example.fitnesspj.domain.user.User;
import org.example.fitnesspj.domain.user.UserRepository;
import org.example.fitnesspj.domain.workout.Workout;
import org.example.fitnesspj.domain.workout.WorkoutRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
}
