package com.bpmplatform.security.domain.repository;

import com.bpmplatform.security.domain.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByTenantId(UUID tenantId);
    List<User> findAll();
    long countByTenantIdAndPrimaryRoleName(UUID tenantId, String roleName);
    void delete(User user);
}
