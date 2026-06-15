package com.bpmplatform.api.dto.document;

import jakarta.validation.constraints.NotBlank;

public record CreateMetadataSchemaRequest(@NotBlank String code, @NotBlank String name, String description, @NotBlank String schemaJson) {}
