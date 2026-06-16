package com.bpmplatform.document.domain.repository;

import com.bpmplatform.document.domain.entity.DocumentInstance;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentInstanceRepository {
    DocumentInstance save(DocumentInstance instance);
    Optional<DocumentInstance> findById(UUID id);
    Optional<DocumentInstance> findByFolio(String folio);
    List<DocumentInstance> findAll();
}
