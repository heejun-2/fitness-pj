package org.example.fitnesspj.domain.record;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SetRecordRepository extends JpaRepository<SetRecord, Long> {
    @Query("""
        select sr
        from SetRecord sr
        join sr.workout w
        where sr.id = :setRecordId
          and w.id = :workoutId
          and w.user.id = :userId
    """)
    Optional<SetRecord> findByIdAndWorkoutIdAndUserId(
            // 수정 대상 setRecordId
            @Param("setRecordId") Long setRecordId,

            // URL의 workoutId와 매칭 검증
            @Param("workoutId") Long workoutId,

            // 본인 데이터만 수정 가능하도록 userId 검증
            @Param("userId") Long userId
    );
}
