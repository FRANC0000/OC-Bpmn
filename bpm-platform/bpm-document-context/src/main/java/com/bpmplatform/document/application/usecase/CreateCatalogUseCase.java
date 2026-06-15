package com.bpmplatform.document.application.usecase;

import com.bpmplatform.common.application.UseCase;
import com.bpmplatform.document.domain.entity.Catalog;
import com.bpmplatform.document.domain.repository.CatalogRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@UseCase
public class CreateCatalogUseCase {

    private final CatalogRepository repository;

    public CreateCatalogUseCase(CatalogRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Output execute(Input input) {
        if (repository.existsByCode(input.code())) {
            throw new IllegalArgumentException("Catalog already exists: " + input.code());
        }

        var catalog = new Catalog(UUID.randomUUID(), input.code(), input.name(), input.description());
        repository.save(catalog);

        return new Output(catalog.getId(), catalog.getCode(), catalog.getName());
    }

    public record Input(String code, String name, String description) {}
    public record Output(UUID id, String code, String name) {}
}
