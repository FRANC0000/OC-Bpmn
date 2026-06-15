package com.bpmplatform.api.dto.audit;

import java.time.Instant;
import java.util.UUID;

public record AuditLogResponse(UUID id, UUID userId, String action, String entityType,
                                String entityId, String detailsJson, String ipAddress, Instant createdAt) {}
