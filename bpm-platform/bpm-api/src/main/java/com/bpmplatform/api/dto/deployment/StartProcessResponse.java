package com.bpmplatform.api.dto.deployment;

public record StartProcessResponse(long processInstanceKey, String bpmnProcessId, int version) {}
