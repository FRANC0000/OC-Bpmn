package com.bpmplatform.security.infrastructure.persistence.repository;

import com.bpmplatform.security.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleJpaRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(String name);
}
