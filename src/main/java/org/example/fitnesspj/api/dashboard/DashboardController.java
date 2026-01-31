package org.example.fitnesspj.api.dashboard;

import lombok.RequiredArgsConstructor;
import org.example.fitnesspj.api.dashboard.dto.DashboardResponse;
import org.example.fitnesspj.application.dashboard.DashboardService;
import org.example.fitnesspj.global.security.UserPrincipal;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponse> dashboard(@AuthenticationPrincipal UserPrincipal principal,
                                                       @RequestParam("weekStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {

        return ResponseEntity.ok(dashboardService.getDashboard(principal.getUserId(), weekStart));

    }
}
