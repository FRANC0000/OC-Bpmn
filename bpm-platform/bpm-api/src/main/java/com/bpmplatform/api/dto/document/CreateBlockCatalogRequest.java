package com.bpmplatform.api.dto.document;

import jakarta.validation.constraints.NotBlank;

public record CreateBlockCatalogRequest(@NotBlank String code, @NotBlank String name, String description) {}
