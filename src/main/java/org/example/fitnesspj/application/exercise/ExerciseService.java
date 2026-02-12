package org.example.fitnesspj.application.exercise;

import lombok.RequiredArgsConstructor;
import org.example.fitnesspj.api.exercise.dto.ExerciseResponse;
import org.example.fitnesspj.domain.exercise.ExerciseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    public List<ExerciseResponse> findAll() {
        return exerciseRepository.findAllByOrderByNameAsc()
                .stream()
                .map(ExerciseResponse::from)
                .toList();

    }}
