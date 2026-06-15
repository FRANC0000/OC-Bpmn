package com.bpmplatform.api.dto.document;

import java.util.UUID;

public record SubmitDocumentResponse(UUID instanceId, String folio, String status) {}
