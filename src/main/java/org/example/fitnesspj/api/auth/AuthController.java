package org.example.fitnesspj.api.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fitnesspj.api.auth.dto.LoginRequest;
import org.example.fitnesspj.api.auth.dto.LoginResponse;
import org.example.fitnesspj.application.auth.AuthService;
import org.example.fitnesspj.global.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        String token = authService.login(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(new LoginResponse(token));
    }

    @GetMapping("/me")
    public Map<String, Object> me(@AuthenticationPrincipal UserPrincipal principal) {
        return Map.of(
                "userId", principal.getUserId(),
                "email", principal.getEmail()
        );
    }
}
