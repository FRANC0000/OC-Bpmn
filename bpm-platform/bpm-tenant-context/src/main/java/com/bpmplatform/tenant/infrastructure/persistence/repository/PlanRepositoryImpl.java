package com.bpmplatform.tenant.infrastructure.persistence.repository;

import com.bpmplatform.tenant.domain.entity.Plan;
import com.bpmplatform.tenant.domain.repository.PlanRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PlanRepositoryImpl implements PlanRepository {

    private final PlanJpaRepository jpaRepository;

    public PlanRepositoryImpl(PlanJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Plan> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Plan> findByCode(String code) {
        return jpaRepository.findByCode(code);
    }
}
