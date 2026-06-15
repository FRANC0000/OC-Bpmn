package com.bpmplatform.document.domain.repository;

import com.bpmplatform.document.domain.entity.BlockCatalog;

import java.util.Optional;
import java.util.UUID;

public interface BlockCatalogRepository {
    BlockCatalog save(BlockCatalog catalog);
    Optional<BlockCatalog> findById(UUID id);
    Optional<BlockCatalog> findByCode(String code);
    boolean existsByCode(String code);
}
