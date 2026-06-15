package com.bpmplatform.api.dto.document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AddBlocksRequest(
        @NotNull UUID documentId,
        @NotNull UUID versionId,
        @NotBlank String blocksJson
) {}
