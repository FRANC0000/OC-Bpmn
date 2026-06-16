package com.bpmplatform.security.infrastructure.persistence.repository;

import com.bpmplatform.security.domain.entity.Role;
import com.bpmplatform.security.domain.repository.RoleRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleJpaRepository jpaRepository;

    public RoleRepositoryImpl(RoleJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Role save(Role role) {
        return jpaRepository.save(role);
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return jpaRepository.findByName(name);
    }

    @Override
    public List<Role> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public void delete(Role role) {
        jpaRepository.delete(role);
    }
}
