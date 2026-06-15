package com.bpmplatform.process.domain.event;

import com.bpmplatform.common.domain.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record ProcessCreatedEvent(UUID eventId, UUID processId, String name, String slug, Instant occurredOn) implements DomainEvent {
    public ProcessCreatedEvent {
        if (eventId == null) eventId = UUID.randomUUID();
        if (occurredOn == null) occurredOn = Instant.now();
    }
}
