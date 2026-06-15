package com.bpmplatform.process.domain.repository;

import com.bpmplatform.process.domain.entity.ProcessTemplate;

import java.util.Optional;
import java.util.UUID;

public interface ProcessTemplateRepository {
    ProcessTemplate save(ProcessTemplate template);
    Optional<ProcessTemplate> findById(UUID id);
}
