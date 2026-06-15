package com.bpmplatform.infrastructure.client;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.DeploymentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.Map;

@Component
@ConditionalOnBean(ZeebeClient.class)
public class ProcessDeployer {

    private static final Logger log = LoggerFactory.getLogger(ProcessDeployer.class);

    private final ZeebeClient client;

    public ProcessDeployer(ZeebeClient client) {
        this.client = client;
    }

    public DeploymentEvent deployFromClasspath(String classpathResource) {
        log.info("Deploying process from classpath: {}", classpathResource);
        var event = client.newDeployResourceCommand()
                .addResourceFromClasspath(classpathResource)
                .send()
                .join();
        log.info("Deployed process '{}' with key {}", event.getProcesses().getFirst().getBpmnProcessId(), event.getKey());
        return event;
    }

    public DeploymentEvent deployFromBytes(String resourceName, byte[] bpmnBytes) {
        log.info("Deploying process from bytes: {}", resourceName);
        var event = client.newDeployResourceCommand()
                .addResourceStream(new ByteArrayInputStream(bpmnBytes), resourceName)
                .send()
                .join();
        log.info("Deployed process '{}' with key {}", event.getProcesses().getFirst().getBpmnProcessId(), event.getKey());
        return event;
    }
}
