package com.bpmplatform.api.dto.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record RegisterUserRequest(
        @Email @NotBlank String email,
        @NotBlank String password,
        @NotBlank String displayName,
        UUID tenantId
) {}
