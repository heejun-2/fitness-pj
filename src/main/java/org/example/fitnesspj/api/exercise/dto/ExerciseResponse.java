package org.example.fitnesspj.api.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.fitnesspj.domain.exercise.Exercise;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseResponse {
    private Long id;
    private String name;
    private String category;

    public static ExerciseResponse from(Exercise e){
        return new ExerciseResponse(e.getId(), e.getName(), e.getCategory());
    }
}
