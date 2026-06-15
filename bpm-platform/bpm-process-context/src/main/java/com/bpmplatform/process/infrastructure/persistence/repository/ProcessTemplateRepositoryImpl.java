package com.bpmplatform.process.infrastructure.persistence.repository;

import com.bpmplatform.process.domain.entity.ProcessTemplate;
import com.bpmplatform.process.domain.repository.ProcessTemplateRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ProcessTemplateRepositoryImpl implements ProcessTemplateRepository {

    private final ProcessTemplateJpaRepository jpaRepository;

    public ProcessTemplateRepositoryImpl(ProcessTemplateJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ProcessTemplate save(ProcessTemplate template) {
        return jpaRepository.save(template);
    }

    @Override
    public Optional<ProcessTemplate> findById(UUID id) {
        return jpaRepository.findById(id);
    }
}
