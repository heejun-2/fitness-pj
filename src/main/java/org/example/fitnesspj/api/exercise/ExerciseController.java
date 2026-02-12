package org.example.fitnesspj.api.exercise;

import lombok.RequiredArgsConstructor;
import org.example.fitnesspj.api.exercise.dto.ExerciseResponse;
import org.example.fitnesspj.application.exercise.ExerciseService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping
    public ResponseEntity<List<ExerciseResponse>> findAll() {
        return ResponseEntity.ok(exerciseService.findAll());
    }
}
