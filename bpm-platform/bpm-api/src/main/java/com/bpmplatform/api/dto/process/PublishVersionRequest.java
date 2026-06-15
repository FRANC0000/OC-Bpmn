package com.bpmplatform.api.dto.process;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PublishVersionRequest(@NotNull UUID processId, @NotNull UUID versionId) {}
