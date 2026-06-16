package com.bpmplatform.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_log")
public class AuditLog extends com.bpmplatform.common.domain.Entity<UUID> {

    @Column(name = "user_id")
    private UUID userId;

    @Column(nullable = false)
    private String action;

    @Column(name = "entity_type", nullable = false)
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private String entityId;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "details_json", columnDefinition = "JSONB")
    private String detailsJson;

    @Column(name = "old_state_json", columnDefinition = "JSONB")
    private String oldStateJson;

    @Column(name = "new_state_json", columnDefinition = "JSONB")
    private String newStateJson;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public AuditLog() {}

    public AuditLog(UUID id, UUID userId, String action, String entityType, String entityId,
                    UUID tenantId, String detailsJson, String oldStateJson, String newStateJson, String ipAddress) {
        super(id);
        this.userId = userId;
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.tenantId = tenantId;
        this.detailsJson = detailsJson;
        this.oldStateJson = oldStateJson;
        this.newStateJson = newStateJson;
        this.ipAddress = ipAddress;
        this.createdAt = Instant.now();
    }

    public UUID getUserId() { return userId; }
    public String getAction() { return action; }
    public String getEntityType() { return entityType; }
    public String getEntityId() { return entityId; }
    public UUID getTenantId() { return tenantId; }
    public String getDetailsJson() { return detailsJson; }
    public String getOldStateJson() { return oldStateJson; }
    public String getNewStateJson() { return newStateJson; }
    public String getIpAddress() { return ipAddress; }
    public Instant getCreatedAt() { return createdAt; }
}
