package com.bpmplatform.api.dto.document;

import java.time.Instant;
import java.util.UUID;

public record DocumentInstanceResponse(
        UUID id, String folio, UUID documentId, String version,
        String status, UUID createdBy, Instant createdAt
) {}