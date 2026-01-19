package org.example.fitnesspj.domain.workout;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findAllByUserIdAndWorkoutDate(Long userId, LocalDate date);
}
