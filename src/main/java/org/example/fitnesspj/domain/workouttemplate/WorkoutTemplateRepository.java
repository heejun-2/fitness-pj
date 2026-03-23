package org.example.fitnesspj.domain.workouttemplate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WorkoutTemplateRepository extends JpaRepository<WorkoutTemplate, Long> {

    @Query("""
        select distinct wt
        from WorkoutTemplate wt
        left join fetch wt.templateSets ts
        left join fetch ts.exercise e
        where wt.user.id = :userId
        order by wt.createdAt desc, wt.id desc
    """)
    List<WorkoutTemplate> findAllByUserIdFetchJoin(@Param("userId") Long userId);

    Optional<WorkoutTemplate> findByIdAndUserId(Long id, Long userId);
}
