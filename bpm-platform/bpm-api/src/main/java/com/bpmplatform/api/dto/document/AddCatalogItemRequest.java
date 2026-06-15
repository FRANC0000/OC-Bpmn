package com.bpmplatform.api.dto.document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AddCatalogItemRequest(
        @NotNull UUID catalogId,
        @NotBlank String code,
        @NotBlank String label,
        int sortOrder,
        String metadataJson
) {}
