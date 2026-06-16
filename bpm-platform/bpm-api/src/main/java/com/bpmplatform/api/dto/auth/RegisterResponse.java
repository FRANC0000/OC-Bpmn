package com.bpmplatform.api.dto.auth;

import java.util.UUID;

public record RegisterResponse(UUID userId, UUID tenantId, String email, String displayName) {}
