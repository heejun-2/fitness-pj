package org.example.fitnesspj.api.workouttemplate.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class WorkoutTemplateCreateRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 1000)
    private String memo;

    @Valid
    @Size(min = 1)
    private List<SetRequest> sets;

    @Getter
    @NoArgsConstructor
    public static class SetRequest {

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
