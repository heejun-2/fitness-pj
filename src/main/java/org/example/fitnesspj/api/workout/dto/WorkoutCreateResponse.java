package org.example.fitnesspj.api.workout.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.service.annotation.GetExchange;

@Getter
@AllArgsConstructor
public class WorkoutCreateResponse {
    private Long workoutId;
}
