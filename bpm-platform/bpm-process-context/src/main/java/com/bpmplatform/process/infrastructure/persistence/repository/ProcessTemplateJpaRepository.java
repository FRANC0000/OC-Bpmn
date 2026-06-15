package com.bpmplatform.process.infrastructure.persistence.repository;

import com.bpmplatform.process.domain.entity.ProcessTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProcessTemplateJpaRepository extends JpaRepository<ProcessTemplate, UUID> {
}
