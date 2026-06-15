package com.bpmplatform.document.domain.event;

import com.bpmplatform.common.domain.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record DocumentCompletedEvent(UUID eventId, UUID instanceId, String folio, Instant occurredOn) implements DomainEvent {
    public DocumentCompletedEvent {
        if (eventId == null) eventId = UUID.randomUUID();
        if (occurredOn == null) occurredOn = Instant.now();
    }
}
