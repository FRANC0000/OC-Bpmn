package com.bpmplatform.api.dto.process;

import java.util.UUID;

public record CreateProcessResponse(UUID id, String name, String slug) {}
