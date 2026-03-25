package org.example.fitnesspj.api.workouttemplate;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fitnesspj.api.workouttemplate.dto.WorkoutTemplateCreateRequest;
import org.example.fitnesspj.api.workouttemplate.dto.WorkoutTemplateResponse;
import org.example.fitnesspj.application.workouttemplate.WorkoutTemplateService;
import org.example.fitnesspj.global.security.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workout-templates")
public class WorkoutTemplateController {

    private final WorkoutTemplateService workoutTemplateService;

    @GetMapping
    public ResponseEntity<List<WorkoutTemplateResponse>> getTemplates(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseEntity.ok(workoutTemplateService.getTemplates(principal.getUserId()));
    }

    @PostMapping
    public ResponseEntity<WorkoutTemplateResponse> createTemplate(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody WorkoutTemplateCreateRequest request
    ) {
        WorkoutTemplateResponse response = workoutTemplateService.createTemplate(principal.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{templateId}")
    public ResponseEntity<WorkoutTemplateResponse> updateTemplate(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long templateId,
            @Valid @RequestBody WorkoutTemplateCreateRequest request
    ) {
        return ResponseEntity.ok(
                workoutTemplateService.updateTemplate(principal.getUserId(), templateId, request)
        );
    }

    @DeleteMapping("/{templateId}")
    public ResponseEntity<Void> deleteTemplate(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long templateId
    ) {
        workoutTemplateService.deleteTemplate(principal.getUserId(), templateId);
        return ResponseEntity.noContent().build();
    }
}
