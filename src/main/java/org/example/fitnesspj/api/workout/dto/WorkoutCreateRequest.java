package org.example.fitnesspj.api.workout.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class WorkoutCreateRequest {

    @NotNull
    private LocalDate workoutDate;

    private String memo;

    @Valid
    @Size(min = 1, message = "세트 기록은 최소 1개 이상이어야 합니다.")
    private List<SetCreateRequest> sets;

    @Getter
    @NoArgsConstructor
    public static class SetCreateRequest {

        @NotNull
        private Long exerciseId;

        @NotNull
        private Integer weight;

        @NotNull
        private Integer reps;

        @NotNull
        private Integer setOrder;
    }
}
