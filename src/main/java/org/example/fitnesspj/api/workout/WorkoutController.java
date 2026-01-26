package org.example.fitnesspj.api.workout;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fitnesspj.api.workout.dto.WorkoutCreateRequest;
import org.example.fitnesspj.api.workout.dto.WorkoutCreateResponse;
import org.example.fitnesspj.api.workout.dto.WorkoutResponse;
import org.example.fitnesspj.application.workout.WorkoutService;
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

    @GetMapping
    public ResponseEntity<List<WorkoutResponse>> getByDate(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return ResponseEntity.ok(workoutService.getWorkoutsByDate(principal.getUserId(), date));
    }

}
