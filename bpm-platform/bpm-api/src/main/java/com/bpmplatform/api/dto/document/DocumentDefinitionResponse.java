package com.bpmplatform.api.dto.document;

import java.util.UUID;

public record DocumentDefinitionResponse(
        UUID id, String code, String name, String description,
        String status, int versionCount
) {}