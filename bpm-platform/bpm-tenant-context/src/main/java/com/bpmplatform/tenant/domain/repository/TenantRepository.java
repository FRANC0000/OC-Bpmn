package com.bpmplatform.tenant.domain.repository;

import com.bpmplatform.tenant.domain.entity.Tenant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenantRepository {
    Tenant save(Tenant tenant);
    Optional<Tenant> findById(UUID id);
    Optional<Tenant> findBySlug(String slug);
    boolean existsBySlug(String slug);
    List<Tenant> findAll();
}
