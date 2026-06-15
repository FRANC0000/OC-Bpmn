package com.bpmplatform.api.dto.document;

import java.util.UUID;

public record AddCatalogItemResponse(UUID id, String code, String label) {}
