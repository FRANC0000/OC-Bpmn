package com.bpmplatform.document.domain.entity;

import com.bpmplatform.common.domain.AggregateRoot;
import com.bpmplatform.document.domain.event.MetadataSchemaCreatedEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "metadata_schemas")
public class MetadataSchema extends AggregateRoot<UUID> {

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "schema_json", nullable = false, columnDefinition = "JSONB")
    private String schemaJson;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public MetadataSchema() {}

    public MetadataSchema(UUID id, String code, String name, String description, String schemaJson) {
        super(id);
        this.code = code;
        this.name = name;
        this.description = description;
        this.schemaJson = schemaJson;
        this.createdAt = Instant.now();
        registerEvent(new MetadataSchemaCreatedEvent(UUID.randomUUID(), id, code, name, createdAt));
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getSchemaJson() { return schemaJson; }
    public boolean isActive() { return isActive; }
    public Instant getCreatedAt() { return createdAt; }

    public void setDescription(String description) { this.description = description; }
    public void setSchemaJson(String schemaJson) { this.schemaJson = schemaJson; }
    public void deactivate() { this.isActive = false; }
}
