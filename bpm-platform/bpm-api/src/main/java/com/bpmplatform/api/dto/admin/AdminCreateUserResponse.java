package com.bpmplatform.api.dto.admin;

import java.util.UUID;

public record AdminCreateUserResponse(UUID userId, UUID tenantId, String email, String displayName) {}
