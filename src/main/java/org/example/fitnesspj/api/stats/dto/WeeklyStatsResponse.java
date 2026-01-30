package org.example.fitnesspj.api.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class WeeklyStatsResponse {

    private LocalDate weekStart;
    private LocalDate weekEnd;

    private long workoutDays;   // 운동한 날짜 수 (distinct date)
    private long workoutCount;  // workout 개수
    private long setCount;      // set_record 개수
    private long totalVolume;   // 총 볼륨 Σ(weight * reps)

    private List<CategoryVolume> volumeByCategory;

    @Getter
    @AllArgsConstructor
    public static class CategoryVolume {
        private String category;
        private long volume;
    }
}
