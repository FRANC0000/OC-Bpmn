package com.bpmplatform.security.infrastructure.persistence.repository;

import com.bpmplatform.security.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByTenantId(UUID tenantId);
    long countByTenantIdAndPrimaryRoleName(UUID tenantId, String roleName);
}
