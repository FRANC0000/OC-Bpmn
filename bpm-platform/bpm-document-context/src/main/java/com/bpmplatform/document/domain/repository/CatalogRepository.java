package com.bpmplatform.document.domain.repository;

import com.bpmplatform.document.domain.entity.Catalog;

import java.util.Optional;
import java.util.UUID;

public interface CatalogRepository {
    Catalog save(Catalog catalog);
    Optional<Catalog> findById(UUID id);
    Optional<Catalog> findByCode(String code);
    boolean existsByCode(String code);
}
