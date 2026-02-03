package org.example.fitnesspj.api.workout.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SetRecordUpdateRequest {
    @Min(value = 0, message = "weight는 0 이상이어야 합니다.")
    private Integer weight;

    @Min(value = 0, message = "reps는 0 이상이어야 합니다.")
    private Integer reps;
}
