package com.bpmplatform.document.infrastructure.persistence.repository;

import com.bpmplatform.document.domain.entity.DocumentDefinition;
import com.bpmplatform.document.domain.repository.DocumentDefinitionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class DocumentDefinitionRepositoryImpl implements DocumentDefinitionRepository {

    private final DocumentDefinitionJpaRepository jpaRepository;

    public DocumentDefinitionRepositoryImpl(DocumentDefinitionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public DocumentDefinition save(DocumentDefinition definition) {
        return jpaRepository.save(definition);
    }

    @Override
    public Optional<DocumentDefinition> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<DocumentDefinition> findByCode(String code) {
        return jpaRepository.findByCode(code);
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }
}
