package com.bpmplatform.document.infrastructure.persistence.repository;

import com.bpmplatform.document.domain.entity.MetadataSchema;
import com.bpmplatform.document.domain.repository.MetadataSchemaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class MetadataSchemaRepositoryImpl implements MetadataSchemaRepository {

    private final MetadataSchemaJpaRepository jpaRepository;

    public MetadataSchemaRepositoryImpl(MetadataSchemaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public MetadataSchema save(MetadataSchema schema) {
        return jpaRepository.save(schema);
    }

    @Override
    public Optional<MetadataSchema> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<MetadataSchema> findByCode(String code) {
        return jpaRepository.findByCode(code);
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }
}
