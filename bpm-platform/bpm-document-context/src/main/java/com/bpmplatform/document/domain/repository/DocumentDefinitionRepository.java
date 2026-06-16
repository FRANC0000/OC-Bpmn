package com.bpmplatform.document.domain.repository;

import com.bpmplatform.document.domain.entity.DocumentDefinition;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentDefinitionRepository {
    DocumentDefinition save(DocumentDefinition definition);
    Optional<DocumentDefinition> findById(UUID id);
    Optional<DocumentDefinition> findByCode(String code);
    boolean existsByCode(String code);
    List<DocumentDefinition> findAll();
}
