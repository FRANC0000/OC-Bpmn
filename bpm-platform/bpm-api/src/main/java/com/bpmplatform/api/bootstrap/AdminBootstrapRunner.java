package com.bpmplatform.api.bootstrap;

import com.bpmplatform.infrastructure.multitenant.TenantContext;
import com.bpmplatform.security.application.usecase.AssignRoleUseCase;
import com.bpmplatform.security.domain.entity.Role;
import com.bpmplatform.security.domain.repository.RoleRepository;
import com.bpmplatform.security.domain.repository.UserRepository;
import com.bpmplatform.security.domain.valueobject.RoleType;
import com.bpmplatform.tenant.domain.repository.TenantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AdminBootstrapRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminBootstrapRunner.class);

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AssignRoleUseCase assignRoleUseCase;
    private final String adminEmail;

    public AdminBootstrapRunner(TenantRepository tenantRepository,
                                UserRepository userRepository,
                                RoleRepository roleRepository,
                                AssignRoleUseCase assignRoleUseCase,
                                @Value("${app.admin.email:f.teran.p22@gmail.com}") String adminEmail) {
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.assignRoleUseCase = assignRoleUseCase;
        this.adminEmail = adminEmail;
    }

    @Override
    public void run(ApplicationArguments args) {
        var tenants = tenantRepository.findAll();

        for (var tenant : tenants) {
            TenantContext.setTenantId(tenant.getSchemaName());
            try {
                var userOpt = userRepository.findByEmail(adminEmail);
                if (userOpt.isEmpty()) {
                    continue;
                }

                var user = userOpt.get();
                if (user.getPrimaryRole() != null && "ADMIN".equals(user.getPrimaryRole().getName())) {
                    log.info("Admin user {} already has ADMIN role in tenant {}", adminEmail, tenant.getName());
                    return;
                }

                var role = roleRepository.findByName("ADMIN")
                        .orElseGet(() -> roleRepository.save(
                                new Role(UUID.randomUUID(), "ADMIN", RoleType.PRIMARY)));

                assignRoleUseCase.assignPrimary(user.getId(), role.getId());
                log.info("Assigned ADMIN role to user {} in tenant {}", adminEmail, tenant.getName());
                return;
            } finally {
                TenantContext.clear();
            }
        }
    }
}
