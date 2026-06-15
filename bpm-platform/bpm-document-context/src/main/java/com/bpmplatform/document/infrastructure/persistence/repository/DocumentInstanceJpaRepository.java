package com.bpmplatform.document.infrastructure.persistence.repository;

import com.bpmplatform.document.domain.entity.DocumentInstance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DocumentInstanceJpaRepository extends JpaRepository<DocumentInstance, UUID> {
    Optional<DocumentInstance> findByFolio(String folio);
}
