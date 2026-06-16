package com.bpmplatform.api.dto.admin;

import java.util.UUID;

public record TenantSummaryResponse(
        UUID id, String name, String slug, String schemaName,
        String plan, String status
) {}
