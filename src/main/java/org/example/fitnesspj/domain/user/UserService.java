package org.example.fitnesspj.domain.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.fitnesspj.global.exception.BusinessException;
import org.example.fitnesspj.global.exception.ErrorCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long signUp(String email, String password, String nickname) {

        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(password);

        User user = User.create(email, encodedPassword, nickname);
        userRepository.save(user);

        return user.getId();
    }
}
