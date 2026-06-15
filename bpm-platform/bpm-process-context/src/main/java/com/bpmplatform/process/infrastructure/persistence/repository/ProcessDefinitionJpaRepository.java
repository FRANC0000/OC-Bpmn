package com.bpmplatform.process.infrastructure.persistence.repository;

import com.bpmplatform.process.domain.entity.ProcessDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProcessDefinitionJpaRepository extends JpaRepository<ProcessDefinition, UUID> {
    Optional<ProcessDefinition> findBySlug(String slug);
    boolean existsBySlug(String slug);
}
