package com.bpmplatform.api.dto.admin;

import java.util.UUID;

public record RoleResponse(
        UUID id,
        String name,
        String description,
        String permissions,
        String roleType,
        boolean isSystem
) {}
