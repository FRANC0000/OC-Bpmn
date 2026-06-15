package com.bpmplatform.tenant.infrastructure.persistence.repository;

import com.bpmplatform.tenant.domain.entity.Tenant;
import com.bpmplatform.tenant.domain.repository.TenantRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class TenantRepositoryImpl implements TenantRepository {

    private final TenantJpaRepository jpaRepository;

    public TenantRepositoryImpl(TenantJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Tenant save(Tenant tenant) {
        return jpaRepository.save(tenant);
    }

    @Override
    public Optional<Tenant> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Tenant> findBySlug(String slug) {
        return jpaRepository.findBySlug(slug);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return jpaRepository.existsBySlug(slug);
    }
}
