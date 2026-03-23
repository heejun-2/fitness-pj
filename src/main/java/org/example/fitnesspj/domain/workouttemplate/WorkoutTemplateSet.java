package org.example.fitnesspj.domain.workouttemplate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.fitnesspj.domain.exercise.Exercise;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class WorkoutTemplateSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_template_id", nullable = false)
    private WorkoutTemplate workoutTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    private int weight;
    private int reps;
    private int setOrder;

    public static WorkoutTemplateSet create(Exercise exercise, int weight, int reps, int setOrder) {
        WorkoutTemplateSet templateSet = new WorkoutTemplateSet();
        templateSet.exercise = exercise;
        templateSet.weight = weight;
        templateSet.reps = reps;
        templateSet.setOrder = setOrder;
        return templateSet;
    }

    public void assignTemplate(WorkoutTemplate workoutTemplate) {
        this.workoutTemplate = workoutTemplate;
    }
}
