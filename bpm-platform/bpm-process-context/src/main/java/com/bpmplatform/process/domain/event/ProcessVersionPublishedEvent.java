package com.bpmplatform.process.domain.event;

import com.bpmplatform.common.domain.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record ProcessVersionPublishedEvent(UUID eventId, UUID processId, UUID versionId, String version, Instant occurredOn) implements DomainEvent {
    public ProcessVersionPublishedEvent {
        if (eventId == null) eventId = UUID.randomUUID();
        if (occurredOn == null) occurredOn = Instant.now();
    }
}
