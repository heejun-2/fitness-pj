package org.example.fitnesspj.api.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExercisePrResponse {
    // 운동 종목 ID
    private Long exerciseId;

    // 운동 종목 이름 (벤치프레스, 스쿼트 등)
    private String exerciseName;

    // 해당 종목에서 기록한 최고 중량(PR)
    private int bestWeight;
}
