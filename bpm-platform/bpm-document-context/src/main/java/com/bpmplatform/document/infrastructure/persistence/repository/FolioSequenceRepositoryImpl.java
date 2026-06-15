package com.bpmplatform.document.infrastructure.persistence.repository;

import com.bpmplatform.document.domain.entity.FolioSequence;
import com.bpmplatform.document.domain.repository.FolioSequenceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class FolioSequenceRepositoryImpl implements FolioSequenceRepository {

    private final FolioSequenceJpaRepository jpaRepository;

    public FolioSequenceRepositoryImpl(FolioSequenceJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public FolioSequence save(FolioSequence sequence) {
        return jpaRepository.save(sequence);
    }

    @Override
    public Optional<FolioSequence> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<FolioSequence> findByFormat(String format) {
        return jpaRepository.findByFormat(format);
    }
}
