package org.example.fitnesspj.api.workout.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class WorkoutResponse {
    private Long workoutId;
    private LocalDate workoutDate;
    private String memo;
    private List<SetResponse> sets;

    @Getter
    @Builder
    public static class SetResponse {
        private Long setRecordId;
        private int setOrder;
        private int weight;
        private int reps;

        private Long exerciseId;
        private String exerciseName;
        private String exerciseCategory;
    }
}
