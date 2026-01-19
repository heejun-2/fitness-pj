package org.example.fitnesspj.domain.exercise;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.fitnesspj.global.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Exercise extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String category;

    public static Exercise create(String name, String category) {
        Exercise exercise = new Exercise();
        exercise.name = name;
        exercise.category = category;
        return exercise;
    }
}
