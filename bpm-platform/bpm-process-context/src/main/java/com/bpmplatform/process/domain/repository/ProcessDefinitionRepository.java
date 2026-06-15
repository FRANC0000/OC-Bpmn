package com.bpmplatform.process.domain.repository;

import com.bpmplatform.process.domain.entity.ProcessDefinition;

import java.util.Optional;
import java.util.UUID;

public interface ProcessDefinitionRepository {
    ProcessDefinition save(ProcessDefinition definition);
    Optional<ProcessDefinition> findById(UUID id);
    Optional<ProcessDefinition> findBySlug(String slug);
    boolean existsBySlug(String slug);
}
