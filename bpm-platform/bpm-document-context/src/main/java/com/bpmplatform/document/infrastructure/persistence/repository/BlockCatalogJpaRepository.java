package com.bpmplatform.document.infrastructure.persistence.repository;

import com.bpmplatform.document.domain.entity.BlockCatalog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BlockCatalogJpaRepository extends JpaRepository<BlockCatalog, UUID> {
    Optional<BlockCatalog> findByCode(String code);
    boolean existsByCode(String code);
}
