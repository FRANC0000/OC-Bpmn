package com.bpmplatform.document.application.usecase;

import com.bpmplatform.common.application.UseCase;
import com.bpmplatform.common.domain.DomainEventPublisher;
import com.bpmplatform.document.domain.entity.DocumentDefinition;
import com.bpmplatform.document.domain.repository.DocumentDefinitionRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@UseCase
public class CreateDocumentDefinitionUseCase {

    private final DocumentDefinitionRepository repository;
    private final DomainEventPublisher eventPublisher;

    public CreateDocumentDefinitionUseCase(DocumentDefinitionRepository repository, DomainEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Output execute(Input input) {
        if (repository.existsByCode(input.code())) {
            throw new IllegalArgumentException("Code already in use: " + input.code());
        }

        var definition = new DocumentDefinition(UUID.randomUUID(), input.code(), input.name());
        definition.setDescription(input.description());

        repository.save(definition);
        definition.clearEvents().forEach(eventPublisher::publish);

        return new Output(definition.getId(), definition.getCode(), definition.getName());
    }

    public record Input(String code, String name, String description) {}
    public record Output(UUID id, String code, String name) {}
}
