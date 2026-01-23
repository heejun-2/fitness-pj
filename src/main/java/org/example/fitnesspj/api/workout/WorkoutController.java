package org.example.fitnesspj.api.workout;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fitnesspj.api.workout.dto.WorkoutCreateRequest;
import org.example.fitnesspj.api.workout.dto.WorkoutCreateResponse;
import org.example.fitnesspj.application.workout.WorkoutService;
import org.example.fitnesspj.global.security.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
