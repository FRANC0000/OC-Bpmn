package com.bpmplatform.api.dto.document;

import jakarta.validation.constraints.NotBlank;

public record CreateDocumentDefinitionRequest(
        @NotBlank String code,
        @NotBlank String name,
        String description
) {}
