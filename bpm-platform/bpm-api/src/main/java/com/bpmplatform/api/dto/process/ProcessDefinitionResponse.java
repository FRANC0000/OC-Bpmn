package com.bpmplatform.api.dto.process;

import java.util.UUID;

public record ProcessDefinitionResponse(
        UUID id, String name, String slug, String description,
        String status, int versionCount, String bpmnXml
) {}