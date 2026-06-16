package com.bpmplatform.api.dto.admin;

import java.time.Instant;
import java.util.UUID;

public record UserSummaryResponse(
        UUID id, String email, String displayName,
        String status, UUID tenantId, Instant lastLoginAt,
        String role
) {}
