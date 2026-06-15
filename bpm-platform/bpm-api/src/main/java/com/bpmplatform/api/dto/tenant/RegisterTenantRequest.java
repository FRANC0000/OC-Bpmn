package com.bpmplatform.api.dto.tenant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterTenantRequest(
        @NotBlank String name,
        @NotBlank @Pattern(regexp = "^[a-z0-9]+(-[a-z0-9]+)*$") String slug,
        @NotBlank String planCode
) {}
