package com.bpmplatform.api.dto.tenant;

import java.util.UUID;

public record RegisterTenantResponse(UUID tenantId, String slug, String schemaName, String plan) {}
