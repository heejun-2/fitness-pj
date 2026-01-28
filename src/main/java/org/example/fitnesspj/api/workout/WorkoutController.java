package org.example.fitnesspj.api.workout;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fitnesspj.api.workout.dto.WorkoutCreateRequest;
import org.example.fitnesspj.api.workout.dto.WorkoutCreateResponse;
import org.example.fitnesspj.api.workout.dto.WorkoutDailyGroupResponse;
import org.example.fitnesspj.api.workout.dto.WorkoutResponse;
import org.example.fitnesspj.application.workout.WorkoutService;
import org.example.fitnesspj.global.exception.BusinessException;
import org.example.fitnesspj.global.exception.ErrorCode;
import org.example.fitnesspj.global.security.UserPrincipal;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workouts")
public class WorkoutController {
    private final WorkoutService workoutService;

    @PostMapping
    public ResponseEntity<WorkoutCreateResponse> create(@AuthenticationPrincipal UserPrincipal principal, @RequestBody @Valid WorkoutCreateRequest request) {
        Long workoutId = workoutService.createWorkout(principal.getUserId(), request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new WorkoutCreateResponse(workoutId));
    }

    // 날짜별 조회
    @GetMapping(params = "date")
    public ResponseEntity<List<WorkoutResponse>> getByDate(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return ResponseEntity.ok(workoutService.getWorkoutsByDate(principal.getUserId(), date));
    }

    // 기간 조회
    @GetMapping(params = {"from", "to"})
    public ResponseEntity<List<WorkoutDailyGroupResponse>> getByPeriodGrouped(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(workoutService.getWorkoutsGroupedByDate(principal.getUserId(), from, to)
        );
    }

    // 단건 조회
    @GetMapping("/{workoutId}")
    public ResponseEntity<WorkoutResponse> getDetail(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long workoutId) {

        return ResponseEntity.ok(workoutService.getWorkoutDetail(principal.getUserId(), workoutId));
    }

    // 기록 삭제
    @DeleteMapping("/{workoutId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long workoutId) {
        workoutService.deleteWorkout(principal.getUserId(), workoutId);

        return ResponseEntity.noContent().build();
    }

}
