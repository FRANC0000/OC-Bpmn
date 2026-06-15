package com.bpmplatform.document.infrastructure.persistence.repository;

import com.bpmplatform.document.domain.entity.Catalog;
import com.bpmplatform.document.domain.repository.CatalogRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class CatalogRepositoryImpl implements CatalogRepository {

    private final CatalogJpaRepository jpaRepository;

    public CatalogRepositoryImpl(CatalogJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Catalog save(Catalog catalog) {
        return jpaRepository.save(catalog);
    }

    @Override
    public Optional<Catalog> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Catalog> findByCode(String code) {
        return jpaRepository.findByCode(code);
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }
}
