package org.example.fitnesspj.api.workout.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class WorkoutDailyGroupResponse {
    private LocalDate date;
    private List<WorkoutResponse> workouts;
}

