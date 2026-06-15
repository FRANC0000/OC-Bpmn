package com.bpmplatform.document.application.usecase;

import com.bpmplatform.common.application.UseCase;
import com.bpmplatform.common.domain.DomainEventPublisher;
import com.bpmplatform.document.domain.entity.MetadataSchema;
import com.bpmplatform.document.domain.repository.MetadataSchemaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@UseCase
public class CreateMetadataSchemaUseCase {

    private final MetadataSchemaRepository repository;
    private final DomainEventPublisher eventPublisher;

    public CreateMetadataSchemaUseCase(MetadataSchemaRepository repository, DomainEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Output execute(Input input) {
        if (repository.existsByCode(input.code())) {
            throw new IllegalArgumentException("Metadata schema already exists: " + input.code());
        }

        var schema = new MetadataSchema(UUID.randomUUID(), input.code(), input.name(), input.description(), input.schemaJson());
        repository.save(schema);
        schema.clearEvents().forEach(eventPublisher::publish);

        return new Output(schema.getId(), schema.getCode(), schema.getName());
    }

    public record Input(String code, String name, String description, String schemaJson) {}
    public record Output(UUID id, String code, String name) {}
}
