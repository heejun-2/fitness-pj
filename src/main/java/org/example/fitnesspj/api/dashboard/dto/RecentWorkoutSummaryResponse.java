package org.example.fitnesspj.api.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class RecentWorkoutSummaryResponse {
    // 운동 기록 식별자
    private Long workoutId;

    // 운동 날짜
    private LocalDate workoutDate;

    // 운동 메모(없을 수도 있음)
    private String memo;

    // 해당 Workout에 포함된 세트 개수
    private long setCount;

    // 해당 Workout의 총 볼륨(Σ weight*reps)
    private long totalVolume;
}
