package com.bpmplatform.security.application.usecase;

import com.bpmplatform.common.application.UseCase;
import com.bpmplatform.infrastructure.config.JwtService;
import com.bpmplatform.security.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@UseCase
public class LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public Output execute(Input input) {
        var user = userRepository.findByEmail(input.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(input.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        if (!"active".equals(user.getStatus())) {
            throw new IllegalStateException("Account is not active");
        }

        user.markLoggedIn();
        userRepository.save(user);

        var token = jwtService.generateToken(user.getId(), user.getEmail(), user.getTenantId());
        return new Output(token, user.getId(), user.getEmail(), user.getDisplayName());
    }

    public record Input(String email, String password) {}
    public record Output(String token, UUID userId, String email, String displayName) {}
}
