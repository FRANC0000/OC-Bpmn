package com.bpmplatform.document.infrastructure.persistence.repository;

import com.bpmplatform.document.domain.entity.FolioSequence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FolioSequenceJpaRepository extends JpaRepository<FolioSequence, UUID> {
    Optional<FolioSequence> findByFormat(String format);
}
