package com.bpmplatform.process.application.service;

import com.bpmplatform.infrastructure.client.ProcessInstanceStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@ConditionalOnProperty(name = "app.zeebe.enabled", havingValue = "true")
public class ProcessExecutionService {

    private static final Logger log = LoggerFactory.getLogger(ProcessExecutionService.class);

    private final ProcessInstanceStarter instanceStarter;

    public ProcessExecutionService(ProcessInstanceStarter instanceStarter) {
        this.instanceStarter = instanceStarter;
    }

    public ExecutionResult startProcess(String bpmnProcessId, Map<String, Object> variables, UUID tenantId) {
        var variablesWithMeta = new java.util.HashMap<>(variables);
        variablesWithMeta.put("tenantId", tenantId.toString());
        variablesWithMeta.put("startedBy", variables.getOrDefault("startedBy", "system"));

        var event = instanceStarter.startProcess(bpmnProcessId, variablesWithMeta);
        log.info("Started process instance {} for '{}' with tenant {}", event.getProcessInstanceKey(), bpmnProcessId, tenantId);

        return new ExecutionResult(event.getProcessInstanceKey(), bpmnProcessId, event.getVersion());
    }

    public record ExecutionResult(long processInstanceKey, String bpmnProcessId, int version) {}
}
