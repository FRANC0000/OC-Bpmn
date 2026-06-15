package com.bpmplatform.security.domain.event;

import com.bpmplatform.common.domain.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record UserRegisteredEvent(UUID eventId, UUID userId, String email, Instant occurredOn) implements DomainEvent {
    public UserRegisteredEvent {
        if (eventId == null) eventId = UUID.randomUUID();
        if (occurredOn == null) occurredOn = Instant.now();
    }
}
