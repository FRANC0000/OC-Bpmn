package com.bpmplatform.document.infrastructure.persistence.repository;

import com.bpmplatform.document.domain.entity.DocumentInstance;
import com.bpmplatform.document.domain.repository.DocumentInstanceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class DocumentInstanceRepositoryImpl implements DocumentInstanceRepository {

    private final DocumentInstanceJpaRepository jpaRepository;

    public DocumentInstanceRepositoryImpl(DocumentInstanceJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public DocumentInstance save(DocumentInstance instance) {
        return jpaRepository.save(instance);
    }

    @Override
    public Optional<DocumentInstance> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<DocumentInstance> findByFolio(String folio) {
        return jpaRepository.findByFolio(folio);
    }
}
