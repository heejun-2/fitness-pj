package org.example.fitnesspj.api.users;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fitnesspj.domain.user.UserService;
import org.example.fitnesspj.domain.user.dto.SignUpRequest;
import org.example.fitnesspj.domain.user.dto.SignUpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody @Valid SignUpRequest request) {
        Long userId = userService.signUp(request.getEmail(), request.getPassword(), request.getNickname());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new SignUpResponse(userId));
    }
}
