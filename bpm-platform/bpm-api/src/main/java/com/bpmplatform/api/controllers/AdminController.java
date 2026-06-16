package com.bpmplatform.api.controllers;

import com.bpmplatform.api.dto.ApiResponse;
import com.bpmplatform.api.dto.admin.AdminCreateUserRequest;
import com.bpmplatform.api.dto.admin.AdminCreateUserResponse;
import com.bpmplatform.api.dto.admin.CreateRoleRequest;
import com.bpmplatform.api.dto.admin.RoleRequest;
import com.bpmplatform.api.dto.admin.RoleResponse;
import com.bpmplatform.api.dto.admin.StatusRequest;
import com.bpmplatform.api.dto.admin.TenantSummaryResponse;
import com.bpmplatform.api.dto.admin.UpdateRoleRequest;
import com.bpmplatform.api.dto.admin.UpdateTenantRequest;
import com.bpmplatform.api.dto.admin.UpdateUserRequest;
import com.bpmplatform.api.dto.admin.UserSummaryResponse;
import com.bpmplatform.infrastructure.multitenant.TenantContext;
import com.bpmplatform.security.application.usecase.AssignRoleUseCase;
import com.bpmplatform.security.application.usecase.RegisterUserUseCase;
import com.bpmplatform.security.domain.entity.Role;
import com.bpmplatform.security.domain.repository.RoleRepository;
import com.bpmplatform.security.domain.repository.UserRepository;
import com.bpmplatform.tenant.application.usecase.RegisterTenantUseCase;
import com.bpmplatform.tenant.domain.repository.TenantRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RegisterUserUseCase registerUserUseCase;
    private final RegisterTenantUseCase registerTenantUseCase;
    private final AssignRoleUseCase assignRoleUseCase;

    public AdminController(TenantRepository tenantRepository,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           RegisterUserUseCase registerUserUseCase,
                           RegisterTenantUseCase registerTenantUseCase,
                           AssignRoleUseCase assignRoleUseCase) {
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.registerUserUseCase = registerUserUseCase;
        this.registerTenantUseCase = registerTenantUseCase;
        this.assignRoleUseCase = assignRoleUseCase;
    }

    // ===== TENANTS =====

    @GetMapping("/tenants")
    public ResponseEntity<ApiResponse<List<TenantSummaryResponse>>> listTenants() {
        String previousTenant = TenantContext.getTenantId();
        try {
            TenantContext.clear();
            var tenants = tenantRepository.findAll().stream()
                    .map(t -> new TenantSummaryResponse(
                            t.getId(), t.getName(), t.getSlug(), t.getSchemaName(),
                            t.getPlan(), t.getStatus()))
                    .toList();
            return ResponseEntity.ok(ApiResponse.ok("Tenants retrieved", tenants));
        } finally {
            if (previousTenant != null) {
                TenantContext.setTenantId(previousTenant);
            }
        }
    }

    @PostMapping("/tenants")
    public ResponseEntity<ApiResponse<RegisterTenantUseCase.Output>> createTenant(
            @Valid @RequestBody RegisterTenantUseCase.Input request) {
        var output = registerTenantUseCase.execute(request);
        log.info("Admin created tenant {} ({})", output.tenantId(), output.slug());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tenant created", output));
    }

    @PutMapping("/tenants/{id}")
    public ResponseEntity<ApiResponse<TenantSummaryResponse>> updateTenant(
            @PathVariable UUID id, @Valid @RequestBody UpdateTenantRequest request) {
        var tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found: " + id));

        String previousTenant = TenantContext.getTenantId();
        try {
            TenantContext.clear();
            var existing = tenantRepository.findBySlug(request.slug());
            if (existing.isPresent() && !existing.get().getId().equals(id)) {
                throw new IllegalArgumentException("Slug already in use: " + request.slug());
            }
        } finally {
            if (previousTenant != null) {
                TenantContext.setTenantId(previousTenant);
            }
        }

        tenant.setName(request.name());
        tenant.setSlug(request.slug());
        tenantRepository.save(tenant);

        var response = new TenantSummaryResponse(
                tenant.getId(), tenant.getName(), tenant.getSlug(), tenant.getSchemaName(),
                tenant.getPlan(), tenant.getStatus());
        return ResponseEntity.ok(ApiResponse.ok("Tenant updated", response));
    }

    @PatchMapping("/tenants/{id}/status")
    public ResponseEntity<ApiResponse<TenantSummaryResponse>> updateTenantStatus(
            @PathVariable UUID id, @Valid @RequestBody StatusRequest request) {
        var tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found: " + id));

        switch (request.status()) {
            case "active" -> tenant.activate();
            case "suspended" -> tenant.suspend();
            default -> throw new IllegalArgumentException("Invalid status: " + request.status());
        }
        tenantRepository.save(tenant);

        var response = new TenantSummaryResponse(
                tenant.getId(), tenant.getName(), tenant.getSlug(), tenant.getSchemaName(),
                tenant.getPlan(), tenant.getStatus());
        return ResponseEntity.ok(ApiResponse.ok("Tenant status updated", response));
    }

    // ===== USERS =====

    @GetMapping("/tenants/{id}/users")
    public ResponseEntity<ApiResponse<List<UserSummaryResponse>>> listTenantUsers(@PathVariable UUID id) {
        var tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found: " + id));

        TenantContext.setTenantId(tenant.getSchemaName());
        try {
            var users = userRepository.findByTenantId(id).stream()
                    .map(u -> new UserSummaryResponse(
                            u.getId(), u.getEmail(), u.getDisplayName(),
                            u.getStatus(), u.getTenantId(), u.getLastLoginAt(),
                            u.getPrimaryRole() != null ? u.getPrimaryRole().getName() : null))
                    .toList();
            return ResponseEntity.ok(ApiResponse.ok("Users retrieved", users));
        } finally {
            TenantContext.clear();
        }
    }

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<AdminCreateUserResponse>> createUser(
            @Valid @RequestBody AdminCreateUserRequest request) {
        var tenant = tenantRepository.findById(request.tenantId())
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found: " + request.tenantId()));

        TenantContext.setTenantId(tenant.getSchemaName());
        try {
            var input = new RegisterUserUseCase.Input(
                    request.email(), request.password(), request.displayName(), request.tenantId());
            var output = registerUserUseCase.execute(input);
            var response = new AdminCreateUserResponse(
                    output.userId(), request.tenantId(), output.email(), output.displayName());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("User created", response));
        } finally {
            TenantContext.clear();
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserSummaryResponse>> updateUser(
            @PathVariable UUID id, @RequestParam UUID tenantId, @Valid @RequestBody UpdateUserRequest request) {
        var schema = "tenant_" + tenantId.toString().replace("-", "");
        TenantContext.setTenantId(schema);
        try {
            var user = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

            user.setDisplayName(request.displayName());
            user.setEmail(request.email());
            userRepository.save(user);

            var response = new UserSummaryResponse(
                    user.getId(), user.getEmail(), user.getDisplayName(),
                    user.getStatus(), user.getTenantId(), user.getLastLoginAt(),
                    user.getPrimaryRole() != null ? user.getPrimaryRole().getName() : null);
            return ResponseEntity.ok(ApiResponse.ok("User updated", response));
        } finally {
            TenantContext.clear();
        }
    }

    @PatchMapping("/users/{id}/status")
    public ResponseEntity<ApiResponse<UserSummaryResponse>> updateUserStatus(
            @PathVariable UUID id, @RequestParam UUID tenantId, @Valid @RequestBody StatusRequest request) {
        var schema = "tenant_" + tenantId.toString().replace("-", "");
        TenantContext.setTenantId(schema);
        try {
            var user = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

            if ("inactive".equals(request.status()) && user.getPrimaryRole() != null
                    && "ADMIN".equals(user.getPrimaryRole().getName())) {
                long adminCount = userRepository.countByTenantIdAndPrimaryRoleName(user.getTenantId(), "ADMIN");
                if (adminCount <= 1) {
                    throw new IllegalArgumentException("Cannot deactivate the last ADMIN of the tenant");
                }
            }

            switch (request.status()) {
                case "active" -> user.activate();
                case "inactive" -> user.deactivate();
                default -> throw new IllegalArgumentException("Invalid status: " + request.status());
            }
            userRepository.save(user);

            var response = new UserSummaryResponse(
                    user.getId(), user.getEmail(), user.getDisplayName(),
                    user.getStatus(), user.getTenantId(), user.getLastLoginAt(),
                    user.getPrimaryRole() != null ? user.getPrimaryRole().getName() : null);
            return ResponseEntity.ok(ApiResponse.ok("User status updated", response));
        } finally {
            TenantContext.clear();
        }
    }

    @PatchMapping("/users/{id}/role")
    public ResponseEntity<ApiResponse<UserSummaryResponse>> updateUserRole(
            @PathVariable UUID id, @RequestParam UUID tenantId, @Valid @RequestBody RoleRequest request) {
        var schema = "tenant_" + tenantId.toString().replace("-", "");
        TenantContext.setTenantId(schema);
        try {
            var user = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

            if (!"ADMIN".equals(request.role()) && !"USER".equals(request.role())) {
                throw new IllegalArgumentException("Invalid role: " + request.role());
            }

            if (!"ADMIN".equals(request.role()) && user.getPrimaryRole() != null
                    && "ADMIN".equals(user.getPrimaryRole().getName())) {
                long adminCount = userRepository.countByTenantIdAndPrimaryRoleName(user.getTenantId(), "ADMIN");
                if (adminCount <= 1) {
                    throw new IllegalArgumentException("Cannot remove the last ADMIN role from the tenant");
                }
            }

            var role = roleRepository.findByName(request.role())
                    .orElseGet(() -> roleRepository.save(new Role(UUID.randomUUID(), request.role())));
            assignRoleUseCase.assignPrimary(user.getId(), role.getId());
            userRepository.save(user);

            var saved = userRepository.findById(id).orElseThrow();
            var response = new UserSummaryResponse(
                    saved.getId(), saved.getEmail(), saved.getDisplayName(),
                    saved.getStatus(), saved.getTenantId(), saved.getLastLoginAt(),
                    saved.getPrimaryRole() != null ? saved.getPrimaryRole().getName() : null);
            return ResponseEntity.ok(ApiResponse.ok("User role updated", response));
        } finally {
            TenantContext.clear();
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id, @RequestParam UUID tenantId) {
        var schema = "tenant_" + tenantId.toString().replace("-", "");
        TenantContext.setTenantId(schema);
        try {
            var user = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

            if (user.getPrimaryRole() != null && "ADMIN".equals(user.getPrimaryRole().getName())) {
                long adminCount = userRepository.countByTenantIdAndPrimaryRoleName(user.getTenantId(), "ADMIN");
                if (adminCount <= 1) {
                    throw new IllegalArgumentException("Cannot delete the last ADMIN of the tenant");
                }
            }

            userRepository.delete(user);
            return ResponseEntity.ok(ApiResponse.ok("User deleted", null));
        } finally {
            TenantContext.clear();
        }
    }

    // ===== ROLES =====

    private String tenantFromAuth() {
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof com.bpmplatform.security.infrastructure.auth.BpmUserDetails user) {
            var tid = user.getTenantId();
            if (tid != null) {
                return "tenant_" + tid.toString().replace("-", "");
            }
        }
        var ctx = TenantContext.getTenantId();
        if (ctx != null && !"public".equals(ctx)) {
            return ctx;
        }
        return "public";
    }

    @GetMapping("/roles")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> listRoles() {
        var schema = tenantFromAuth();
        TenantContext.setTenantId(schema);
        try {
            var roles = roleRepository.findAll().stream()
                    .map(r -> new RoleResponse(
                            r.getId(), r.getName(), r.getDescription(),
                            r.getPermissions(), r.getRoleType(), r.isSystem()))
                    .toList();
            return ResponseEntity.ok(ApiResponse.ok("Roles retrieved", roles));
        } finally {
            TenantContext.clear();
        }
    }

    @PostMapping("/roles")
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(@Valid @RequestBody CreateRoleRequest request) {
        if (roleRepository.findByName(request.name()).isPresent()) {
            throw new IllegalArgumentException("Role already exists: " + request.name());
        }
        var schema = tenantFromAuth();
        TenantContext.setTenantId(schema);
        try {
            var role = new Role(UUID.randomUUID(), request.name());
            role.setDescription(request.description() != null ? request.description() : "");
            role.setPermissions(request.permissions() != null ? request.permissions() : "[]");
            role.setRoleType(request.roleType() != null ? request.roleType() : "secondary");
            roleRepository.save(role);
            var response = new RoleResponse(
                    role.getId(), role.getName(), role.getDescription(),
                    role.getPermissions(), role.getRoleType(), role.isSystem());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("Role created", response));
        } finally {
            TenantContext.clear();
        }
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> updateRole(
            @PathVariable UUID id, @Valid @RequestBody UpdateRoleRequest request) {
        var schema = tenantFromAuth();
        TenantContext.setTenantId(schema);
        try {
            var role = roleRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found: " + id));

            if (role.isSystem()) {
                throw new IllegalArgumentException("Cannot modify system role: " + role.getName());
            }

            var existing = roleRepository.findByName(request.name());
            if (existing.isPresent() && !existing.get().getId().equals(id)) {
                throw new IllegalArgumentException("Role name already in use: " + request.name());
            }

            role.setName(request.name());
            role.setDescription(request.description() != null ? request.description() : "");
            role.setPermissions(request.permissions() != null ? request.permissions() : "[]");
            role.setRoleType(request.roleType() != null ? request.roleType() : "secondary");
            roleRepository.save(role);

            var response = new RoleResponse(
                    role.getId(), role.getName(), role.getDescription(),
                    role.getPermissions(), role.getRoleType(), role.isSystem());
            return ResponseEntity.ok(ApiResponse.ok("Role updated", response));
        } finally {
            TenantContext.clear();
        }
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable UUID id) {
        var schema = tenantFromAuth();
        TenantContext.setTenantId(schema);
        try {
            var role = roleRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found: " + id));

            if (role.isSystem()) {
                throw new IllegalArgumentException("Cannot delete system role: " + role.getName());
            }

            roleRepository.delete(role);
            return ResponseEntity.ok(ApiResponse.ok("Role deleted", null));
        } finally {
            TenantContext.clear();
        }
    }
}
