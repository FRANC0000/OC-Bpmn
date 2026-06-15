package com.bpmplatform.api.dto.document;

import java.util.UUID;

public record SaveDraftResponse(UUID instanceId, String folio, String status) {}
