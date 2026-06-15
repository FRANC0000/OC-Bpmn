package com.bpmplatform.infrastructure.client;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConditionalOnBean(ZeebeClient.class)
public class ProcessInstanceStarter {

    private static final Logger log = LoggerFactory.getLogger(ProcessInstanceStarter.class);

    private final ZeebeClient client;

    public ProcessInstanceStarter(ZeebeClient client) {
        this.client = client;
    }

    public ProcessInstanceEvent startProcess(String bpmnProcessId, Map<String, Object> variables) {
        log.info("Starting process instance for '{}' with {} variables", bpmnProcessId, variables.size());
        var event = client.newCreateInstanceCommand()
                .bpmnProcessId(bpmnProcessId)
                .latestVersion()
                .variables(variables)
                .send()
                .join();
        log.info("Started process instance {} for '{}'", event.getProcessInstanceKey(), bpmnProcessId);
        return event;
    }

    public ProcessInstanceEvent startProcessWithTenant(String bpmnProcessId, Map<String, Object> variables, String tenantId) {
        log.info("Starting process instance for '{}' with tenant {}", bpmnProcessId, tenantId);
        var variablesWithTenant = new java.util.HashMap<>(variables);
        variablesWithTenant.put("tenantId", tenantId);

        var event = client.newCreateInstanceCommand()
                .bpmnProcessId(bpmnProcessId)
                .latestVersion()
                .variables(variablesWithTenant)
                .send()
                .join();
        log.info("Started process instance {} for tenant {}", event.getProcessInstanceKey(), tenantId);
        return event;
    }
}
