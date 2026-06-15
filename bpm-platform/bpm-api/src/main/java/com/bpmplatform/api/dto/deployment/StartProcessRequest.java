package com.bpmplatform.api.dto.deployment;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;
import java.util.UUID;

public record StartProcessRequest(
        @NotBlank String bpmnProcessId,
        Map<String, Object> variables,
        UUID tenantId
) {}
