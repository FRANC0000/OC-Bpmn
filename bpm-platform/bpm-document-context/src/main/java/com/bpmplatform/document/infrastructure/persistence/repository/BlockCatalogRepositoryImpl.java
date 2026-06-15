package com.bpmplatform.document.infrastructure.persistence.repository;

import com.bpmplatform.document.domain.entity.BlockCatalog;
import com.bpmplatform.document.domain.repository.BlockCatalogRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class BlockCatalogRepositoryImpl implements BlockCatalogRepository {

    private final BlockCatalogJpaRepository jpaRepository;

    public BlockCatalogRepositoryImpl(BlockCatalogJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public BlockCatalog save(BlockCatalog catalog) {
        return jpaRepository.save(catalog);
    }

    @Override
    public Optional<BlockCatalog> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<BlockCatalog> findByCode(String code) {
        return jpaRepository.findByCode(code);
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }
}
