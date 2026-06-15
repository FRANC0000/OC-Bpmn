package com.bpmplatform.document.domain.event;

import com.bpmplatform.common.domain.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record DocumentSubmittedEvent(UUID eventId, UUID instanceId, String folio, Instant occurredOn) implements DomainEvent {
    public DocumentSubmittedEvent {
        if (eventId == null) eventId = UUID.randomUUID();
        if (occurredOn == null) occurredOn = Instant.now();
    }
}
