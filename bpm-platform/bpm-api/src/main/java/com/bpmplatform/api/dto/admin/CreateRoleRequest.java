package com.bpmplatform.api.dto.admin;

import jakarta.validation.constraints.NotBlank;

public record CreateRoleRequest(
        @NotBlank String name,
        String description,
        String permissions,
        String roleType
) {}
