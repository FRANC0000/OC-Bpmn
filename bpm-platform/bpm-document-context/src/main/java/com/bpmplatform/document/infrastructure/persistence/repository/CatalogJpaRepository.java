package com.bpmplatform.document.infrastructure.persistence.repository;

import com.bpmplatform.document.domain.entity.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CatalogJpaRepository extends JpaRepository<Catalog, UUID> {
    Optional<Catalog> findByCode(String code);
    boolean existsByCode(String code);
}
