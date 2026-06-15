package com.bpmplatform.api.dto.deployment;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record DeployProcessRequest(@NotNull UUID processId, @NotNull UUID versionId) {}
