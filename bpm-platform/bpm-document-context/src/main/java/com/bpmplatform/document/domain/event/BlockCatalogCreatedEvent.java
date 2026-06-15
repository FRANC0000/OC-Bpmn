package com.bpmplatform.document.domain.event;

import com.bpmplatform.common.domain.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record BlockCatalogCreatedEvent(UUID eventId, UUID catalogId, String code, String name, Instant occurredOn) implements DomainEvent {
    public BlockCatalogCreatedEvent {
        if (eventId == null) eventId = UUID.randomUUID();
        if (occurredOn == null) occurredOn = Instant.now();
    }
}
