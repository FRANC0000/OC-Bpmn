package com.bpmplatform.infrastructure.messaging;

import com.bpmplatform.common.domain.DomainEvent;
import com.bpmplatform.infrastructure.multitenant.TenantContext;
import com.bpmplatform.infrastructure.persistence.AuditLog;
import com.bpmplatform.infrastructure.persistence.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuditEventListener {

    private final AuditLogRepository auditLogRepository;
    private final HttpServletRequest request;

    public AuditEventListener(AuditLogRepository auditLogRepository, HttpServletRequest request) {
        this.auditLogRepository = auditLogRepository;
        this.request = request;
    }

    @EventListener
    public void onDomainEvent(DomainEvent event) {
        var action = event.getClass().getSimpleName()
                .replace("Event", "")
                .replaceAll("([a-z])([A-Z])", "$1_$2")
                .toLowerCase();

        var tenantId = TenantContext.getTenantId()
                .map(id -> {
                    try { return UUID.fromString(id.replace("tenant_", "")); }
                    catch (Exception e) { return null; }
                })
                .orElse(null);

        var ip = request.getRemoteAddr();

        var log = new AuditLog(
                UUID.randomUUID(), null, action,
                event.getClass().getSimpleName(), event.eventId().toString(),
                tenantId, null, null, null, ip);

        auditLogRepository.save(log);
    }
}
