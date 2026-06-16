package com.bpmplatform.security.infrastructure.persistence.repository;

import com.bpmplatform.security.domain.entity.User;
import com.bpmplatform.security.domain.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;

    public UserRepositoryImpl(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User save(User user) {
        return jpaRepository.save(user);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public List<User> findByTenantId(UUID tenantId) {
        return jpaRepository.findByTenantId(tenantId);
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public long countByTenantIdAndPrimaryRoleName(UUID tenantId, String roleName) {
        return jpaRepository.countByTenantIdAndPrimaryRoleName(tenantId, roleName);
    }

    @Override
    public void delete(User user) {
        jpaRepository.delete(user);
    }
}
