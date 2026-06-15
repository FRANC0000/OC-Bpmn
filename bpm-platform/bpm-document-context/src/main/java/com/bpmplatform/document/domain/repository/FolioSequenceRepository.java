package com.bpmplatform.document.domain.repository;

import com.bpmplatform.document.domain.entity.FolioSequence;

import java.util.Optional;
import java.util.UUID;

public interface FolioSequenceRepository {
    FolioSequence save(FolioSequence sequence);
    Optional<FolioSequence> findById(UUID id);
    Optional<FolioSequence> findByFormat(String format);
}
