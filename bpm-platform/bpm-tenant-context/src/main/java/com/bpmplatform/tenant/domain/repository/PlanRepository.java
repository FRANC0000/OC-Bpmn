package com.bpmplatform.tenant.domain.repository;

import com.bpmplatform.tenant.domain.entity.Plan;

import java.util.Optional;
import java.util.UUID;

public interface PlanRepository {
    Optional<Plan> findById(UUID id);
    Optional<Plan> findByCode(String code);
}
