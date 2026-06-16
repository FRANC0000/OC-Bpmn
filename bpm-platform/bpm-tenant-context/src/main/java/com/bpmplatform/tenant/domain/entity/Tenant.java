package com.bpmplatform.tenant.domain.entity;

import com.bpmplatform.common.domain.AggregateRoot;
import com.bpmplatform.tenant.domain.valueobject.PlanCode;
import com.bpmplatform.tenant.domain.valueobject.SchemaName;
import com.bpmplatform.tenant.domain.valueobject.Slug;
import com.bpmplatform.tenant.domain.valueobject.TenantStatus;
import com.bpmplatform.tenant.domain.event.TenantRegisteredEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tenant_registry", schema = "public")
public class Tenant extends AggregateRoot<UUID> {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(name = "schema_name", nullable = false, unique = true)
    private String schemaName;

    @Column(nullable = false)
    private String plan;

    @Column(nullable = false)
    private String status;

    public Tenant() {}

    public Tenant(UUID id, String name, Slug slug, SchemaName schemaName, PlanCode plan) {
        super(id);
        this.name = name;
        this.slug = slug.value();
        this.schemaName = schemaName.value();
        this.plan = plan.code();
        this.status = TenantStatus.ACTIVE.value();
        registerEvent(new TenantRegisteredEvent(UUID.randomUUID(), id, this.slug, Instant.now()));
    }

    public String getName() { return name; }
    public String getSlug() { return slug; }
    public String getSchemaName() { return schemaName; }
    public String getPlan() { return plan; }
    public String getStatus() { return status; }

    public void setName(String name) { this.name = name; }
    public void setSlug(String slug) { this.slug = slug; }

    public PlanCode getPlanCode() { return PlanCode.fromString(plan); }
    public TenantStatus getTenantStatus() { return TenantStatus.fromString(status); }

    public void changePlan(PlanCode newPlan) {
        this.plan = newPlan.code();
    }

    public void activate() {
        this.status = TenantStatus.ACTIVE.value();
    }

    public void suspend() {
        this.status = TenantStatus.SUSPENDED.value();
    }
}
