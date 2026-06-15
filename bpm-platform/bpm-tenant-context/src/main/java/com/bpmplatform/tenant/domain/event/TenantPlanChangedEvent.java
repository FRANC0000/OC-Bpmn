package com.bpmplatform.tenant.domain.event;

import com.bpmplatform.common.domain.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record TenantPlanChangedEvent(UUID eventId, UUID tenantId, String oldPlan, String newPlan, Instant occurredOn) implements DomainEvent {
    public TenantPlanChangedEvent {
        if (eventId == null) eventId = UUID.randomUUID();
        if (occurredOn == null) occurredOn = Instant.now();
    }
}
