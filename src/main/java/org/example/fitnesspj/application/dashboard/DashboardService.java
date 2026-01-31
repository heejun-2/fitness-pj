package org.example.fitnesspj.application.dashboard;

import lombok.RequiredArgsConstructor;
import org.example.fitnesspj.api.dashboard.dto.DashboardResponse;
import org.example.fitnesspj.api.stats.dto.ExercisePrResponse;
import org.example.fitnesspj.api.stats.dto.WeeklyStatsResponse;
import org.example.fitnesspj.application.stats.StatsService;
import org.example.fitnesspj.application.workout.WorkoutService;
import org.example.fitnesspj.global.exception.BusinessException;
import org.example.fitnesspj.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final StatsService statsService;

    public DashboardResponse getDashboard(Long userId, LocalDate weekStart) {

        // 입력 검증: weekStart는 월요일만 허용
        if(weekStart.getDayOfWeek() != DayOfWeek.MONDAY) {
            throw new BusinessException(ErrorCode.INVALID_WEEK_START);
        }

        // 주간 통계 조회
        WeeklyStatsResponse weeklyStats = statsService.getWeeklyStats(userId, weekStart);

        // 종목별 PR 조회
        List<ExercisePrResponse> prs = statsService.getExercisePrs(userId);

        return DashboardResponse.builder()
                .weeklyStats(weeklyStats)
                .exercisePrs(prs)
                .build();
    }
}
