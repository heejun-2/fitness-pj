package org.example.fitnesspj.application.stats;

import lombok.RequiredArgsConstructor;
import org.example.fitnesspj.api.stats.dto.ExercisePrResponse;
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

    // 주간 통계
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

    // 종목별 최고 중량 조회
    public List<ExercisePrResponse> getExercisePrs(Long userId) {

        // DB에서 종목별 최고 중량을 집계해서 가져옴
        List<Object[]> rows = workoutRepository.findExercisePrs(userId);

        List<ExercisePrResponse> result = new ArrayList<>();

        // Object[] → DTO로 변환
        for (Object[] row : rows) {

            Long exerciseId = (Long) row[0];      // e.id
            String name     = (String) row[1];    // e.name
            int bestWeight  = ((Number) row[2]).intValue(); // max(weight)

            result.add(new ExercisePrResponse(exerciseId, name, bestWeight));
        }

        return result;
    }
}
