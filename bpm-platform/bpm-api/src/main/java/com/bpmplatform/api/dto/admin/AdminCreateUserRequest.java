package com.bpmplatform.api.dto.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AdminCreateUserRequest(
        @Email @NotBlank String email,
        @NotBlank String password,
        @NotBlank String displayName,
        @NotNull UUID tenantId
) {}
