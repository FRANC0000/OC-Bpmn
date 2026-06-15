package com.bpmplatform.document.infrastructure.persistence.repository;

import com.bpmplatform.document.domain.entity.MetadataSchema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MetadataSchemaJpaRepository extends JpaRepository<MetadataSchema, UUID> {
    Optional<MetadataSchema> findByCode(String code);
    boolean existsByCode(String code);
}
