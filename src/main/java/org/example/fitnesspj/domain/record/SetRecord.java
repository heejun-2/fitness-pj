package org.example.fitnesspj.domain.record;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.fitnesspj.domain.exercise.Exercise;
import org.example.fitnesspj.domain.workout.Workout;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class SetRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id")
    private Workout workout;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;


    private int weight;
    private int reps;
    private int setOrder;


    public static SetRecord create(Exercise exercise, int weight, int reps, int order) {
        SetRecord record = new SetRecord();
        record.exercise = exercise;
        record.weight = weight;
        record.reps = reps;
        record.setOrder = order;
        return record;
    }


    public void assignWorkout(Workout workout) {
        this.workout = workout;
    }
}
