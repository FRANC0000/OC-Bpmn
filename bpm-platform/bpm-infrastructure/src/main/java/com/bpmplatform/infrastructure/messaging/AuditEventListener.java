package com.bpmplatform.infrastructure.messaging;
import com.bpmplatform.common.domain.DomainEvent;
import com.bpmplatform.infrastructure.multitenant.TenantContext;
import com.bpmplatform.infrastructure.persistence.AuditLog;
import com.bpmplatform.infrastructure.persistence.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Component
public class AuditEventListener {

    private static final Logger log = LoggerFactory.getLogger(AuditEventListener.class);
    private final AuditLogRepository auditLogRepository;
    private final HttpServletRequest request;

    public AuditEventListener(AuditLogRepository auditLogRepository, HttpServletRequest request) {
        this.auditLogRepository = auditLogRepository;
        this.request = request;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onDomainEvent(DomainEvent event) {
        try {
            var action = event.getClass().getSimpleName()
                    .replace("Event", "")
                    .replaceAll("([a-z])([A-Z])", "$1_$2")
                    .toLowerCase();

            var tenantIdStr = TenantContext.getTenantId();
            UUID tenantId = null;
            if (tenantIdStr != null && !tenantIdStr.equals("public")) {
                try { tenantId = UUID.fromString(tenantIdStr); }
                catch (Exception e) {
                    try { tenantId = UUID.fromString(tenantIdStr.replace("tenant_", "")); }
                    catch (Exception ignored) {}
                }
            }

            var ip = request.getRemoteAddr();

            var log = new AuditLog(
                    UUID.randomUUID(), null, action,
                    event.getClass().getSimpleName(), event.eventId().toString(),
                    tenantId, null, null, null, ip);

            auditLogRepository.save(log);
        } catch (Exception e) {
            log.warn("Failed to persist audit log for event {}: {}", event.getClass().getSimpleName(), e.getMessage());
        }
    }
}
