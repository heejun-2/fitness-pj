package org.example.fitnesspj.api.dashboard.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.fitnesspj.api.stats.dto.ExercisePrResponse;
import org.example.fitnesspj.api.stats.dto.WeeklyStatsResponse;

import java.util.List;

@Getter
@Builder
public class DashboardResponse {

    // [블록 기능] 주간 요약 통계(운동일수, 총세트, 총볼륨, 카테고리별 볼륨 등)
    private WeeklyStatsResponse weeklyStats;

    // [블록 기능] 종목별 PR(최고중량) 리스트
    private List<ExercisePrResponse> exercisePrs;
}
