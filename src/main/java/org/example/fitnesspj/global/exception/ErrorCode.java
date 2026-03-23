package org.example.fitnesspj.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 400
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "요청 값이 올바르지 않습니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "DUPLICATE_EMAIL", "이미 사용 중인 이메일입니다."),
    INVALID_WEEK_START(HttpStatus.BAD_REQUEST, "INVALID_WEEK_START", "weekStart는 월요일이어야 합니다."),

    // 404
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "리소스를 찾을 수 없습니다."),
    WORKOUT_NOT_FOUND(HttpStatus.NOT_FOUND, "WORKOUT_NOT_FOUND", "운동기록을 찾을 수 없습니다."),
    WORKOUT_TEMPLATE_NOT_FOUND(HttpStatus.NOT_FOUND, "WORKOUT_TEMPLATE_NOT_FOUND", "운동 템플릿을 찾을 수 없습니다."),
    SET_RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "SET_RECORD_NOT_FOUND", "세트 기록을 찾을 수 없습니다."),
    EXERCISE_NOT_FOUND(HttpStatus.NOT_FOUND, "EXERCISE_NOT_FOUND", "운동 종목을 찾을 수 없습니다."),

    // 401/403
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "접근 권한이 없습니다."),

    // 500
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "서버 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
