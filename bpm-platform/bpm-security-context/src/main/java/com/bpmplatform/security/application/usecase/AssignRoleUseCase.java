package com.bpmplatform.security.application.usecase;

import com.bpmplatform.common.application.UseCase;
import com.bpmplatform.security.domain.entity.Role;
import com.bpmplatform.security.domain.repository.RoleRepository;
import com.bpmplatform.security.domain.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@UseCase
public class AssignRoleUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AssignRoleUseCase(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void assignPrimary(UUID userId, UUID roleId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        var role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleId));
        user.assignPrimaryRole(role);
        userRepository.save(user);
    }

    @Transactional
    public void addSecondary(UUID userId, UUID roleId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        var role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleId));
        user.addSecondaryRole(role);
        userRepository.save(user);
    }
}
