package org.example.fitnesspj.application.workout;

import lombok.RequiredArgsConstructor;
import org.example.fitnesspj.api.workout.dto.WorkoutCreateRequest;
import org.example.fitnesspj.domain.exercise.Exercise;
import org.example.fitnesspj.domain.exercise.ExerciseRepository;
import org.example.fitnesspj.domain.record.SetRecord;
import org.example.fitnesspj.domain.user.User;
import org.example.fitnesspj.domain.user.UserRepository;
import org.example.fitnesspj.domain.workout.Workout;
import org.example.fitnesspj.domain.workout.WorkoutRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
