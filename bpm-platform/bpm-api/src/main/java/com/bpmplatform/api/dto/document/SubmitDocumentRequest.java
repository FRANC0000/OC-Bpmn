package com.bpmplatform.api.dto.document;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record SubmitDocumentRequest(
        @NotBlank String documentCode,
        @NotBlank String valuesJson,
        UUID createdBy
) {}
