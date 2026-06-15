package com.bpmplatform.tenant.domain.entity;

import com.bpmplatform.common.domain.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;
import java.util.UUID;

@jakarta.persistence.Entity
@Table(name = "plans", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = "code")
})
public class Plan extends Entity<UUID> {

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, length = 3)
    private String currency = "USD";

    @Column(name = "max_users")
    private Integer maxUsers;

    @Column(name = "max_processes")
    private Integer maxProcesses;

    @Column(name = "max_instances")
    private Integer maxInstances;

    @Column(name = "max_documents")
    private Integer maxDocuments;

    @Column(name = "max_storage_mb")
    private Long maxStorageMb;

    @Column(name = "sla_percent", nullable = false, precision = 5, scale = 2)
    private BigDecimal slaPercent = BigDecimal.valueOf(99.5);

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    public Plan() {}

    public Plan(UUID id, String code, String name, BigDecimal price) {
        super(id);
        this.code = code;
        this.name = name;
        this.price = price;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public String getCurrency() { return currency; }
    public Integer getMaxUsers() { return maxUsers; }
    public Integer getMaxProcesses() { return maxProcesses; }
    public Integer getMaxInstances() { return maxInstances; }
    public Integer getMaxDocuments() { return maxDocuments; }
    public Long getMaxStorageMb() { return maxStorageMb; }
    public BigDecimal getSlaPercent() { return slaPercent; }
    public boolean isActive() { return isActive; }

    public void setDescription(String description) { this.description = description; }
    public void setMaxUsers(Integer maxUsers) { this.maxUsers = maxUsers; }
    public void setMaxProcesses(Integer maxProcesses) { this.maxProcesses = maxProcesses; }
    public void setMaxInstances(Integer maxInstances) { this.maxInstances = maxInstances; }
    public void setMaxDocuments(Integer maxDocuments) { this.maxDocuments = maxDocuments; }
    public void setMaxStorageMb(Long maxStorageMb) { this.maxStorageMb = maxStorageMb; }
}
