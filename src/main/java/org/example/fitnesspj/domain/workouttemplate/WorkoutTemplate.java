package org.example.fitnesspj.domain.workouttemplate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.fitnesspj.domain.user.User;
import org.example.fitnesspj.global.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class WorkoutTemplate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 1000)
    private String memo;

    @OneToMany(mappedBy = "workoutTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<WorkoutTemplateSet> templateSets = new ArrayList<>();

    public static WorkoutTemplate create(User user, String name, String memo) {
        WorkoutTemplate template = new WorkoutTemplate();
        template.user = user;
        template.name = name;
        template.memo = memo;
        return template;
    }

    public void addTemplateSet(WorkoutTemplateSet templateSet) {
        templateSets.add(templateSet);
        templateSet.assignTemplate(this);
    }

    public void changeDetails(String name, String memo) {
        this.name = name;
        this.memo = memo;
    }

    public void clearTemplateSets() {
        templateSets.clear();
    }
}
