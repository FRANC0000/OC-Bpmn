package com.bpmplatform.api.dto.document;

import java.util.UUID;

public record CreateDocumentDefinitionResponse(UUID id, String code, String name) {}
