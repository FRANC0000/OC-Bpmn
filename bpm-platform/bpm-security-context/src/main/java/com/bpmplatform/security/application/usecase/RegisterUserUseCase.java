package com.bpmplatform.security.application.usecase;

import com.bpmplatform.common.application.UseCase;
import com.bpmplatform.common.domain.DomainEventPublisher;
import com.bpmplatform.security.domain.entity.User;
import com.bpmplatform.security.domain.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@UseCase
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final DomainEventPublisher eventPublisher;

    public RegisterUserUseCase(UserRepository userRepository, DomainEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Output execute(Input input) {
        if (userRepository.existsByEmail(input.email())) {
            throw new IllegalArgumentException("Email already in use: " + input.email());
        }

        var user = new User(UUID.randomUUID(), input.email(), input.passwordHash(),
                input.displayName(), input.tenantId());

        userRepository.save(user);
        user.clearEvents().forEach(eventPublisher::publish);

        return new Output(user.getId(), user.getEmail(), user.getDisplayName());
    }

    public record Input(String email, String passwordHash, String displayName, UUID tenantId) {}
    public record Output(UUID userId, String email, String displayName) {}
}
