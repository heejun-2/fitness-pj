package org.example.fitnesspj.api.workout;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fitnesspj.api.workout.dto.*;
import org.example.fitnesspj.application.workout.WorkoutService;
import org.example.fitnesspj.global.exception.BusinessException;
import org.example.fitnesspj.global.exception.ErrorCode;
import org.example.fitnesspj.global.security.UserPrincipal;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    // 날짜별 조회
    @GetMapping(params = "date")
    public ResponseEntity<List<WorkoutResponse>> getByDate(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return ResponseEntity.ok(workoutService.getWorkoutsByDate(principal.getUserId(), date));
    }

    // 기간 조회
    @GetMapping(params = {"from", "to"})
    public ResponseEntity<List<WorkoutDailyGroupResponse>> getByPeriodGrouped(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(workoutService.getWorkoutsGroupedByDate(principal.getUserId(), from, to)
        );
    }

    // 단건 조회
    @GetMapping("/{workoutId}")
    public ResponseEntity<WorkoutResponse> getDetail(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long workoutId) {

        return ResponseEntity.ok(workoutService.getWorkoutDetail(principal.getUserId(), workoutId));
    }

    // 기록 삭제
    @DeleteMapping("/{workoutId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long workoutId) {
        workoutService.deleteWorkout(principal.getUserId(), workoutId);

        return ResponseEntity.noContent().build();
    }

    // 운동 기록 수정
    @PutMapping("/{workoutId}")
    public ResponseEntity<Void> update(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long workoutId, @RequestBody @Valid WorkoutCreateRequest request
    ) {
        workoutService.updateWorkout(
                principal.getUserId(),
                workoutId,
                request
        );

        return ResponseEntity.noContent().build();
    }

    // 메모 수정
    @PatchMapping("/{workoutId}/memo")
    public ResponseEntity<WorkoutResponse> updateMemo(@AuthenticationPrincipal UserPrincipal principal,
                                                      @PathVariable Long workoutId,
                                                      @Valid @RequestBody WorkoutMemoUpdateRequest request) {

        return ResponseEntity.ok(workoutService.updateWorkoutMemo(principal.getUserId(), workoutId, request.getMemo()));
    }

    // 세트 수정
    @PatchMapping("/{workoutId}/sets/{setRecordId}")
    public ResponseEntity<WorkoutResponse> updateSet(@AuthenticationPrincipal UserPrincipal principal,
                                                     @PathVariable Long workoutId,
                                                     @PathVariable Long setRecordId,
                                                     @Valid @RequestBody SetRecordUpdateRequest request) {

        return ResponseEntity.ok(workoutService.updateSetRecord(principal.getUserId(), workoutId, setRecordId, request.getWeight(), request.getReps()));
    }

    // 세트 삭제
    @DeleteMapping("/{workoutId}/sets/{setRecordId}")
    public ResponseEntity<Void> deleteSet(@AuthenticationPrincipal UserPrincipal principal,
                                          @PathVariable Long workoutId,
                                          @PathVariable Long setRecordId){

        workoutService.deleteSetRecord(principal.getUserId(), workoutId, setRecordId);

        return ResponseEntity.noContent().build();
    }

    // 세트 추가
    @PostMapping("/{workoutId}/sets")
    public ResponseEntity<WorkoutResponse> addSet(@AuthenticationPrincipal UserPrincipal principal,
                                                  @PathVariable Long workoutId,
                                                  @Valid @RequestBody SetRecordCreateRequest request
    ) {
        return ResponseEntity.ok(
                workoutService.addSetRecord(principal.getUserId(), workoutId, request.getExerciseId(), request.getWeight(), request.getReps()
                )
        );
    }

    // 월별 운동 기록 날짜 조회
    @GetMapping("/dates")
    public ResponseEntity<List<LocalDate>> getWorkoutDates(@AuthenticationPrincipal UserPrincipal principal, @RequestParam int year, @RequestParam int month) {
        List<LocalDate> dates = workoutService.getWorkoutDatesOfMonth(
                principal.getUserId(),
                year,
                month
        );

        return ResponseEntity.ok(dates);
    }
}
