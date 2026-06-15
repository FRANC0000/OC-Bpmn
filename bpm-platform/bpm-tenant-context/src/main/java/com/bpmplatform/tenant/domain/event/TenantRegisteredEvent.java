package com.bpmplatform.tenant.domain.event;

import com.bpmplatform.common.domain.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record TenantRegisteredEvent(UUID eventId, UUID tenantId, String slug, Instant occurredOn) implements DomainEvent {
    public TenantRegisteredEvent {
        if (eventId == null) eventId = UUID.randomUUID();
        if (occurredOn == null) occurredOn = Instant.now();
    }
}
