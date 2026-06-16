package com.bpmplatform.process.application.service;

import com.bpmplatform.infrastructure.client.ProcessDeployer;
import com.bpmplatform.process.domain.entity.ProcessDefinition;
import com.bpmplatform.process.domain.entity.ProcessVersion;
import com.bpmplatform.process.domain.repository.ProcessDefinitionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@ConditionalOnProperty(name = "app.zeebe.enabled", havingValue = "true")
public class ZeebeDeploymentService {

    private static final Logger log = LoggerFactory.getLogger(ZeebeDeploymentService.class);

    private final ProcessDefinitionRepository definitionRepository;
    private final ProcessDeployer processDeployer;

    public ZeebeDeploymentService(ProcessDefinitionRepository definitionRepository, ProcessDeployer processDeployer) {
        this.definitionRepository = definitionRepository;
        this.processDeployer = processDeployer;
    }

    @Transactional
    public DeploymentResult deployVersion(UUID processId, UUID versionId) {
        var definition = definitionRepository.findById(processId)
                .orElseThrow(() -> new IllegalArgumentException("Process not found: " + processId));

        var version = definition.getVersions().stream()
                .filter(v -> v.getId().equals(versionId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId));

        if (!"published".equals(version.getStatus())) {
            throw new IllegalStateException("Version " + version.getVersion() + " must be published before deployment");
        }

        var bpmnBytes = version.getBpmnXml().getBytes(StandardCharsets.UTF_8);
        var resourceName = definition.getSlug() + "-v" + version.getVersion().replace(".", "-") + ".bpmn";

        var deployment = processDeployer.deployFromBytes(resourceName, bpmnBytes);

        var deployedProcess = deployment.getProcesses().getFirst();
        log.info("Process '{}' deployed to Zeebe (key: {}, version: {})",
                deployedProcess.getBpmnProcessId(), deployment.getKey(), deployedProcess.getVersion());

        return new DeploymentResult(
                deployment.getKey(),
                deployedProcess.getBpmnProcessId(),
                deployedProcess.getVersion()
        );
    }

    public record DeploymentResult(long deploymentKey, String bpmnProcessId, int processVersion) {}
}
