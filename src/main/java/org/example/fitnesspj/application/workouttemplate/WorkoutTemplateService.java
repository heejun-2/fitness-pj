package org.example.fitnesspj.application.workouttemplate;

import lombok.RequiredArgsConstructor;
import org.example.fitnesspj.api.workouttemplate.dto.WorkoutTemplateCreateRequest;
import org.example.fitnesspj.api.workouttemplate.dto.WorkoutTemplateResponse;
import org.example.fitnesspj.domain.exercise.Exercise;
import org.example.fitnesspj.domain.exercise.ExerciseRepository;
import org.example.fitnesspj.domain.user.User;
import org.example.fitnesspj.domain.user.UserRepository;
import org.example.fitnesspj.domain.workouttemplate.WorkoutTemplate;
import org.example.fitnesspj.domain.workouttemplate.WorkoutTemplateRepository;
import org.example.fitnesspj.domain.workouttemplate.WorkoutTemplateSet;
import org.example.fitnesspj.global.exception.BusinessException;
import org.example.fitnesspj.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkoutTemplateService {

    private final WorkoutTemplateRepository workoutTemplateRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;

    @Transactional(readOnly = true)
    public List<WorkoutTemplateResponse> getTemplates(Long userId) {
        List<WorkoutTemplate> templates = workoutTemplateRepository.findAllByUserIdFetchJoin(userId);
        templates.sort(
                Comparator.comparing(WorkoutTemplate::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(WorkoutTemplate::getId, Comparator.reverseOrder())
        );
        List<WorkoutTemplateResponse> responses = new ArrayList<>();

        for (WorkoutTemplate template : templates) {
            responses.add(toResponse(template));
        }

        return responses;
    }

    public WorkoutTemplateResponse createTemplate(Long userId, WorkoutTemplateCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        WorkoutTemplate template = WorkoutTemplate.create(user, request.getName(), request.getMemo());
        applyTemplateSets(template, request);

        WorkoutTemplate saved = workoutTemplateRepository.save(template);
        return toResponse(saved);
    }

    public WorkoutTemplateResponse updateTemplate(Long userId, Long templateId, WorkoutTemplateCreateRequest request) {
        WorkoutTemplate template = workoutTemplateRepository.findByIdAndUserId(templateId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.WORKOUT_TEMPLATE_NOT_FOUND));

        template.changeDetails(request.getName(), request.getMemo());
        template.clearTemplateSets();
        applyTemplateSets(template, request);

        return toResponse(template);
    }

    public void deleteTemplate(Long userId, Long templateId) {
        WorkoutTemplate template = workoutTemplateRepository.findByIdAndUserId(templateId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.WORKOUT_TEMPLATE_NOT_FOUND));

        workoutTemplateRepository.delete(template);
    }

    private void applyTemplateSets(WorkoutTemplate template, WorkoutTemplateCreateRequest request) {
        for (WorkoutTemplateCreateRequest.SetRequest setRequest : request.getSets()) {
            Exercise exercise = exerciseRepository.findById(setRequest.getExerciseId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.EXERCISE_NOT_FOUND));

            WorkoutTemplateSet templateSet = WorkoutTemplateSet.create(
                    exercise,
                    setRequest.getWeight(),
                    setRequest.getReps(),
                    setRequest.getSetOrder()
            );
            template.addTemplateSet(templateSet);
        }
    }

    private WorkoutTemplateResponse toResponse(WorkoutTemplate template) {
        List<WorkoutTemplateSet> templateSets = new ArrayList<>(template.getTemplateSets());
        templateSets.sort(Comparator.comparingInt(WorkoutTemplateSet::getSetOrder));

        List<WorkoutTemplateResponse.SetResponse> setResponses = new ArrayList<>();
        for (WorkoutTemplateSet templateSet : templateSets) {
            setResponses.add(
                    WorkoutTemplateResponse.SetResponse.builder()
                            .id(templateSet.getId())
                            .exerciseId(templateSet.getExercise().getId())
                            .exerciseName(templateSet.getExercise().getName())
                            .exerciseCategory(templateSet.getExercise().getCategory())
                            .weight(templateSet.getWeight())
                            .reps(templateSet.getReps())
                            .setOrder(templateSet.getSetOrder())
                            .build()
            );
        }

        return WorkoutTemplateResponse.builder()
                .id(template.getId())
                .name(template.getName())
                .memo(template.getMemo())
                .sets(setResponses)
                .build();
    }
}
