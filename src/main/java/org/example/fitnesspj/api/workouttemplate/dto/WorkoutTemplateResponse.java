package org.example.fitnesspj.api.workouttemplate.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class WorkoutTemplateResponse {
    private Long id;
    private String name;
    private String memo;
    private List<SetResponse> sets;

    @Getter
    @Builder
    public static class SetResponse {
        private Long id;
        private Long exerciseId;
        private String exerciseName;
        private String exerciseCategory;
        private int weight;
        private int reps;
        private int setOrder;
    }
}
