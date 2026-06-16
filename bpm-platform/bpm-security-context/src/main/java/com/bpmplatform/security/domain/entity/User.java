package com.bpmplatform.security.domain.entity;

import com.bpmplatform.common.domain.AggregateRoot;
import com.bpmplatform.security.domain.event.UserRegisteredEvent;
import com.bpmplatform.security.domain.valueobject.Email;
import com.bpmplatform.security.domain.valueobject.PasswordHash;
import com.bpmplatform.security.domain.valueobject.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User extends AggregateRoot<UUID> {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(nullable = false)
    private String status = "active";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "primary_role_id")
    private Role primaryRole;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_secondary_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> secondaryRoles = new HashSet<>();

    public User() {}

    public User(UUID id, String email, String passwordHash, String displayName, UUID tenantId) {
        super(id);
        this.email = email;
        this.passwordHash = passwordHash;
        this.displayName = displayName;
        this.tenantId = tenantId;
        this.status = UserStatus.ACTIVE.value();
        registerEvent(new UserRegisteredEvent(UUID.randomUUID(), id, email, Instant.now()));
    }

    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getDisplayName() { return displayName; }
    public String getStatus() { return status; }
    public Role getPrimaryRole() { return primaryRole; }
    public UUID getTenantId() { return tenantId; }
    public Instant getLastLoginAt() { return lastLoginAt; }
    public Set<Role> getSecondaryRoles() { return secondaryRoles; }

    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public void setEmail(String email) { this.email = email; }

    public void assignPrimaryRole(Role role) {
        this.primaryRole = role;
    }

    public void addSecondaryRole(Role role) {
        this.secondaryRoles.add(role);
    }

    public void markLoggedIn() {
        this.lastLoginAt = Instant.now();
    }

    public void deactivate() {
        this.status = UserStatus.INACTIVE.value();
    }

    public void activate() {
        this.status = UserStatus.ACTIVE.value();
    }
}
