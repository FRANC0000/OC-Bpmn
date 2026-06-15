package com.bpmplatform.document.domain.repository;

import com.bpmplatform.document.domain.entity.MetadataSchema;

import java.util.Optional;
import java.util.UUID;

public interface MetadataSchemaRepository {
    MetadataSchema save(MetadataSchema schema);
    Optional<MetadataSchema> findById(UUID id);
    Optional<MetadataSchema> findByCode(String code);
    boolean existsByCode(String code);
}
