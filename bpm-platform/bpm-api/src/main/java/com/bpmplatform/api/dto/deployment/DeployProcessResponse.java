package com.bpmplatform.api.dto.deployment;

public record DeployProcessResponse(long deploymentKey, String bpmnProcessId, int version) {}
