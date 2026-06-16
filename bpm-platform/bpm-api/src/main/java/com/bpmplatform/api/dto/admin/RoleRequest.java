package com.bpmplatform.api.dto.admin;

import jakarta.validation.constraints.NotBlank;

public record RoleRequest(
        @NotBlank String role
) {}
