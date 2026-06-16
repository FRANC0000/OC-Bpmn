package com.bpmplatform.process.infrastructure.persistence.repository;

import com.bpmplatform.process.domain.entity.ProcessDefinition;
import com.bpmplatform.process.domain.repository.ProcessDefinitionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProcessDefinitionRepositoryImpl implements ProcessDefinitionRepository {

    private final ProcessDefinitionJpaRepository jpaRepository;

    public ProcessDefinitionRepositoryImpl(ProcessDefinitionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ProcessDefinition save(ProcessDefinition definition) {
        return jpaRepository.save(definition);
    }

    @Override
    public Optional<ProcessDefinition> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<ProcessDefinition> findBySlug(String slug) {
        return jpaRepository.findBySlug(slug);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return jpaRepository.existsBySlug(slug);
    }

    @Override
    public List<ProcessDefinition> findAll() {
        return jpaRepository.findAll();
    }
}
