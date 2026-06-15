package com.bpmplatform.security.domain.entity;

import com.bpmplatform.common.domain.Entity;
import com.bpmplatform.security.domain.valueobject.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "roles")
public class Role extends Entity<UUID> {

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Column(nullable = false)
    private String permissions = "[]";

    @Column(name = "role_type", nullable = false)
    private String roleType = "secondary";

    @Column(name = "is_system", nullable = false)
    private boolean isSystem = false;

    public Role() {}

    public Role(UUID id, String name) {
        super(id);
        this.name = name;
    }

    public Role(UUID id, String name, RoleType roleType) {
        super(id);
        this.name = name;
        this.roleType = roleType.value();
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getPermissions() { return permissions; }
    public String getRoleType() { return roleType; }
    public boolean isSystem() { return isSystem; }

    public void setDescription(String description) { this.description = description; }
    public void setPermissions(String permissions) { this.permissions = permissions; }
}
