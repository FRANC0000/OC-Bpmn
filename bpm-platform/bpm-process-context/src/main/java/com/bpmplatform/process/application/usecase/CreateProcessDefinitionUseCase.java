package com.bpmplatform.process.application.usecase;

import com.bpmplatform.common.application.UseCase;
import com.bpmplatform.common.domain.DomainEventPublisher;
import com.bpmplatform.process.domain.entity.ProcessDefinition;
import com.bpmplatform.process.domain.repository.ProcessDefinitionRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@UseCase
public class CreateProcessDefinitionUseCase {

    private final ProcessDefinitionRepository repository;
    private final DomainEventPublisher eventPublisher;

    public CreateProcessDefinitionUseCase(ProcessDefinitionRepository repository, DomainEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Output execute(Input input) {
        if (repository.existsBySlug(input.slug())) {
            throw new IllegalArgumentException("Slug already in use: " + input.slug());
        }

        var definition = new ProcessDefinition(UUID.randomUUID(), input.name(), input.slug(), input.ownerUserId());
        definition.setDescription(input.description());

        repository.save(definition);
        definition.clearEvents().forEach(eventPublisher::publish);

        return new Output(definition.getId(), definition.getName(), definition.getSlug());
    }

    public record Input(String name, String description, String slug, UUID ownerUserId) {}
    public record Output(UUID id, String name, String slug) {}
}
