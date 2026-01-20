package org.example.fitnesspj.api.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fitnesspj.api.auth.dto.LoginRequest;
import org.example.fitnesspj.api.auth.dto.LoginResponse;
import org.example.fitnesspj.application.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
