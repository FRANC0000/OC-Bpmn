package com.bpmplatform.security.domain.entity;

import com.bpmplatform.security.domain.valueobject.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "roles")
public class Role extends com.bpmplatform.common.domain.Entity<UUID> {

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
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

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPermissions(String permissions) { this.permissions = permissions; }
    public void setRoleType(String roleType) { this.roleType = roleType; }
    public void setSystem(boolean isSystem) { this.isSystem = isSystem; }
}
