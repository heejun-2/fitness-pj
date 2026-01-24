package org.example.fitnesspj.global.exception.GlobalExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import org.example.fitnesspj.global.exception.BusinessException;
import org.example.fitnesspj.global.exception.ErrorCode;
import org.example.fitnesspj.global.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1) @Valid 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e, HttpServletRequest request) {

        Map<String, String> errors = new LinkedHashMap<>();

        for (FieldError fe : e.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }

        ErrorCode code = ErrorCode.INVALID_REQUEST;

        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(code.getStatus().value())
                .code(code.getCode())
                .message(code.getMessage())
                .path(request.getRequestURI())
                .errors(errors)
                .build();

        return ResponseEntity.status(code.getStatus()).body(body);
    }

    // 2) 비즈니스 예외
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException e, HttpServletRequest request) {

        ErrorCode code = e.getErrorCode();

        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(code.getStatus().value())
                .code(code.getCode())
                .message(e.getMessage()) // customMessage 지원
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(code.getStatus()).body(body);
    }

    // 3) 인증 실패 (401)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuth(AuthenticationException e, HttpServletRequest request) {

        ErrorCode code = ErrorCode.UNAUTHORIZED;

        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(code.getStatus().value())
                .code(code.getCode())
                .message(code.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(code.getStatus()).body(body);
    }

    // 4) 권한 실패 (403)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException e, HttpServletRequest request) {

        ErrorCode code = ErrorCode.FORBIDDEN;

        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(code.getStatus().value())
                .code(code.getCode())
                .message(code.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(code.getStatus()).body(body);
    }

    // 5) 예상 못한 예외 (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception e, HttpServletRequest request) {

        ErrorCode code = ErrorCode.INTERNAL_ERROR;

        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(code.getStatus().value())
                .code(code.getCode())
                .message(code.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(code.getStatus()).body(body);
    }
}
