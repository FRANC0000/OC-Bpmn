package com.bpmplatform.document.application.usecase;

import com.bpmplatform.common.application.UseCase;
import com.bpmplatform.common.domain.DomainEventPublisher;
import com.bpmplatform.document.domain.entity.BlockCatalog;
import com.bpmplatform.document.domain.repository.BlockCatalogRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@UseCase
public class CreateBlockCatalogUseCase {

    private final BlockCatalogRepository repository;
    private final DomainEventPublisher eventPublisher;

    public CreateBlockCatalogUseCase(BlockCatalogRepository repository, DomainEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Output execute(Input input) {
        if (repository.existsByCode(input.code())) {
            throw new IllegalArgumentException("Block catalog already exists: " + input.code());
        }

        var catalog = new BlockCatalog(UUID.randomUUID(), input.code(), input.name(), input.description());
        repository.save(catalog);
        catalog.clearEvents().forEach(eventPublisher::publish);

        return new Output(catalog.getId(), catalog.getCode(), catalog.getName());
    }

    public record Input(String code, String name, String description) {}
    public record Output(UUID id, String code, String name) {}
}
