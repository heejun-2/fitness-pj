package org.example.fitnesspj.api.stats;

import lombok.RequiredArgsConstructor;
import org.example.fitnesspj.api.stats.dto.ExercisePrResponse;
import org.example.fitnesspj.api.stats.dto.WeeklyStatsResponse;
import org.example.fitnesspj.application.stats.StatsService;
import org.example.fitnesspj.global.security.UserPrincipal;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stats")
public class StatsController {
    private final StatsService statsService;

    @GetMapping("/weekly")
    public ResponseEntity<WeeklyStatsResponse> weekly(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam("weekStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart
    ) {
        return ResponseEntity.ok(statsService.getWeeklyStats(principal.getUserId(), weekStart));
    }

    @GetMapping("/prs")
    public ResponseEntity<List<ExercisePrResponse>> exercisePrs(@AuthenticationPrincipal UserPrincipal principal) {

        // 인증된 사용자 ID로만 PR 조회
        return ResponseEntity.ok(statsService.getExercisePrs(principal.getUserId()));
    }

}
