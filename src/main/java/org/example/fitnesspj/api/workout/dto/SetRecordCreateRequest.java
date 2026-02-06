package org.example.fitnesspj.api.workout.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SetRecordCreateRequest {
    // 운동 종목
    @NotNull(message = "exerciseId는 필수입니다.")
    private Long exerciseId;

    // 중량
    @Min(value = 0, message = "weight는 0 이상이어야 합니다.")
    private int weight;

    // 횟수
    @Min(value = 0, message = "reps는 0 이상이어야 합니다.")
    private int reps;
}
