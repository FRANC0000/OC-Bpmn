package com.bpmplatform.api.dto.admin;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
        @NotBlank String displayName,
        @NotBlank String email
) {}
