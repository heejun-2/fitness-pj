package org.example.fitnesspj.application.stats;

import lombok.RequiredArgsConstructor;
import org.example.fitnesspj.api.stats.dto.WeeklyStatsResponse;
import org.example.fitnesspj.domain.workout.WorkoutRepository;
import org.example.fitnesspj.global.exception.BusinessException;
import org.example.fitnesspj.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsService {
    private final WorkoutRepository workoutRepository;

    public WeeklyStatsResponse getWeeklyStats(Long userId, LocalDate weekStart) {

        // weekStart가 월요인인지 확인
        if (weekStart.getDayOfWeek() != DayOfWeek.MONDAY) {
            throw new BusinessException(ErrorCode.INVALID_WEEK_START);
        }

        LocalDate weekEnd = weekStart.plusDays(6);

        Object[] summary = workoutRepository.findWeeklySummary(userId, weekStart, weekEnd);

        long workoutDays  = ((Number) summary[0]).longValue();
        long workoutCount = ((Number) summary[1]).longValue();
        long setCount     = ((Number) summary[2]).longValue();
        long totalVolume  = ((Number) summary[3]).longValue();

        List<Object[]> rows = workoutRepository.findWeeklyVolumeByCategory(userId, weekStart, weekEnd);
        List<WeeklyStatsResponse.CategoryVolume> byCategory = new ArrayList<>();

        for (Object[] r : rows) {
            String category = (String) r[0];
            long volume = ((Number) r[1]).longValue();
            byCategory.add(new WeeklyStatsResponse.CategoryVolume(category, volume));
        }

        return WeeklyStatsResponse.builder()
                .weekStart(weekStart)
                .weekEnd(weekEnd)
                .workoutDays(workoutDays)
                .workoutCount(workoutCount)
                .setCount(setCount)
                .totalVolume(totalVolume)
                .volumeByCategory(byCategory)
                .build();
    }
}
