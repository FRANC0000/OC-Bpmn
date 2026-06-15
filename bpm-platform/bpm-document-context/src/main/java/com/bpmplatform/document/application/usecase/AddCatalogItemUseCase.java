package com.bpmplatform.document.application.usecase;

import com.bpmplatform.common.application.UseCase;
import com.bpmplatform.document.domain.entity.CatalogItem;
import com.bpmplatform.document.domain.repository.CatalogRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@UseCase
public class AddCatalogItemUseCase {

    private final CatalogRepository repository;

    public AddCatalogItemUseCase(CatalogRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Output execute(Input input) {
        var catalog = repository.findById(input.catalogId())
                .orElseThrow(() -> new IllegalArgumentException("Catalog not found: " + input.catalogId()));

        var item = new CatalogItem(UUID.randomUUID(), input.code(), input.label(), input.sortOrder());
        item.setMetadataJson(input.metadataJson());
        catalog.addItem(item);
        repository.save(catalog);

        return new Output(item.getId(), item.getCode(), item.getLabel());
    }

    public record Input(UUID catalogId, String code, String label, int sortOrder, String metadataJson) {}
    public record Output(UUID id, String code, String label) {}
}
