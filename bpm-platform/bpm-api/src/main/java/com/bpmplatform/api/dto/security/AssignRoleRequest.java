package com.bpmplatform.api.dto.security;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AssignRoleRequest(@NotNull UUID userId, @NotNull UUID roleId) {}
