package com.bpmplatform.document.domain.event;

import com.bpmplatform.common.domain.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record MetadataSchemaCreatedEvent(UUID eventId, UUID schemaId, String code, String name, Instant occurredOn) implements DomainEvent {
    public MetadataSchemaCreatedEvent {
        if (eventId == null) eventId = UUID.randomUUID();
        if (occurredOn == null) occurredOn = Instant.now();
    }
}
