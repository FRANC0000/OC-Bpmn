package com.bpmplatform.api.dto.document;

import java.util.UUID;

public record CreateCatalogResponse(UUID id, String code, String name) {}
