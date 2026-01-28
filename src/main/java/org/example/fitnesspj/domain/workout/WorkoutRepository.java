package org.example.fitnesspj.domain.workout;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findAllByUserIdAndWorkoutDate(Long userId, LocalDate date);

    @Query("""
        select distinct w
        from Workout w
        join fetch w.setRecords sr
        join fetch sr.exercise e
        where w.user.id = :userId
          and w.workoutDate = :date
        order by w.id asc, sr.setOrder asc
    """)
    List<Workout> findAllByUserIdAndDateFetchJoin(@Param("userId") Long userId, @Param("date") LocalDate date);

    @Query("""
        select distinct w
        from Workout w
        join fetch w.setRecords sr
        join fetch sr.exercise e
        where w.user.id = :userId
          and w.workoutDate between :from and :to
        order by w.workoutDate asc, w.id asc, sr.setOrder asc
    """)
    List<Workout> findAllByUserIdAndDateBetweenFetchJoin(@Param("userId") Long userId, @Param("from") LocalDate from, @Param("to") LocalDate to);


    @Query("""
        select distinct w
        from Workout w
        join fetch w.setRecords sr
        join fetch sr.exercise e
        where w.id = :workoutId
          and w.user.id = :userId
        order by sr.setOrder asc
        """)
    Optional<Workout> findDetailByIdAndUserIdFetchJoin(@Param("workoutId") Long workoutId, @Param("userId") Long userId);

    long deleteByIdAndUserId(Long workoutId, Long userId);
}
