package com.bpmplatform.document.infrastructure.persistence.repository;

import com.bpmplatform.document.domain.entity.DocumentDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DocumentDefinitionJpaRepository extends JpaRepository<DocumentDefinition, UUID> {
    Optional<DocumentDefinition> findByCode(String code);
    boolean existsByCode(String code);
}
