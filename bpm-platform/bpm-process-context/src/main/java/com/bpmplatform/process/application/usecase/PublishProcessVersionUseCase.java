package com.bpmplatform.process.application.usecase;

import com.bpmplatform.common.application.UseCase;
import com.bpmplatform.common.domain.DomainEventPublisher;
import com.bpmplatform.process.domain.event.ProcessVersionPublishedEvent;
import com.bpmplatform.process.domain.repository.ProcessDefinitionRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@UseCase
public class PublishProcessVersionUseCase {

    private final ProcessDefinitionRepository definitionRepository;
    private final DomainEventPublisher eventPublisher;

    public PublishProcessVersionUseCase(ProcessDefinitionRepository definitionRepository, DomainEventPublisher eventPublisher) {
        this.definitionRepository = definitionRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void execute(UUID processId, UUID versionId) {
        var definition = definitionRepository.findById(processId)
                .orElseThrow(() -> new IllegalArgumentException("Process not found: " + processId));

        var version = definition.getVersions().stream()
                .filter(v -> v.getId().equals(versionId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId));

        version.publish();
        definition.activate();

        definitionRepository.save(definition);
        eventPublisher.publish(new ProcessVersionPublishedEvent(UUID.randomUUID(), processId, versionId,
                version.getVersion(), Instant.now()));
    }
}
