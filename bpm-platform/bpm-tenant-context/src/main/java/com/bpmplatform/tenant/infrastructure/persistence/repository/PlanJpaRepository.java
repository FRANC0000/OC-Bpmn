package com.bpmplatform.tenant.infrastructure.persistence.repository;

import com.bpmplatform.tenant.domain.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PlanJpaRepository extends JpaRepository<Plan, UUID> {
    Optional<Plan> findByCode(String code);
}
