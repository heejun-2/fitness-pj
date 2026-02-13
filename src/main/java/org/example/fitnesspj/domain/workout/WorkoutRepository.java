package org.example.fitnesspj.domain.workout;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    interface WeeklySummaryView {
        long getWorkoutDays();
        long getWorkoutCount();
        long getSetCount();
        long getTotalVolume();
    }
    List<Workout> findAllByUserIdAndWorkoutDate(Long userId, LocalDate date);

    // 날짜 조회
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


    // 기간 조회
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


    // 단일 조회
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


    // 삭제
    long deleteByIdAndUserId(Long workoutId, Long userId);


    // 주간 통계
    @Query("""
        select
            count(distinct w.workoutDate) as workoutDays,
            count(distinct w.id) as workoutCount,
            count(sr.id) as setCount,
            coalesce(sum(sr.weight * sr.reps), 0) as totalVolume
        from Workout w
        join w.setRecords sr
        where w.user.id = :userId
          and w.workoutDate between :from and :to
    """)
    WeeklySummaryView findWeeklySummary(@Param("userId") Long userId, @Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("""
        select e.category, coalesce(sum(sr.weight * sr.reps), 0)
        from Workout w
        join w.setRecords sr
        join sr.exercise e
        where w.user.id = :userId
          and w.workoutDate between :from and :to
        group by e.category
        order by coalesce(sum(sr.weight * sr.reps), 0) desc
    """)
    List<Object[]> findWeeklyVolumeByCategory(@Param("userId") Long userId, @Param("from") LocalDate from, @Param("to") LocalDate to);


    // 종목별 최고 중량 조회
    @Query("""
        select
            e.id,
            e.name,
            max(sr.weight)
        from Workout w
        join w.setRecords sr
        join sr.exercise e
        where w.user.id = :userId
        group by e.id, e.name
        order by max(sr.weight) desc
    """)
    List<Object[]> findExercisePrs(@Param("userId") Long userId);

    // 이번 주 최근 운동 3개
    @Query("""
            select
                w.id,
                w.workoutDate,
                w.memo,
                count(sr.id),
                coalesce(sum(sr.weight * sr.reps), 0)
            from Workout w
            join w.setRecords sr
            where w.user.id = :userId
              and w.workoutDate between :from and :to
            group by w.id, w.workoutDate, w.memo
            order by w.workoutDate desc, w.id desc
        """)
    List<Object[]> findRecentWorkoutSummariesInPeriod(
            // 사용자 기준 필터
            @Param("userId") Long userId,

            // 주간 범위(weekStart ~ weekEnd)
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,

            // 최근 3개 제한을 위해 Pageable 사용
            Pageable pageable
    );

    // 수정 대상 조회 + 권한 체크(내 기록만)
    Optional<Workout> findByIdAndUserId(Long workoutId, Long userId);

}
