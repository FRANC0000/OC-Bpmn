package com.bpmplatform.api.dto.admin;

import jakarta.validation.constraints.NotBlank;

public record UpdateTenantRequest(
        @NotBlank String name,
        @NotBlank String slug
) {}
