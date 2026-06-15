package com.bpmplatform.document.domain.entity;

import com.bpmplatform.common.domain.AggregateRoot;
import com.bpmplatform.document.domain.event.DocumentSubmittedEvent;
import com.bpmplatform.document.domain.event.DocumentCompletedEvent;
import com.bpmplatform.document.domain.valueobject.InstanceStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "document_instances")
public class DocumentInstance extends AggregateRoot<UUID> {

    @Column(name = "document_id", nullable = false)
    private UUID documentId;

    @Column(nullable = false)
    private String version;

    @Column(nullable = false, unique = true)
    private String folio;

    @Column(nullable = false)
    private String status = "draft";

    @Column(name = "values_json", nullable = false, columnDefinition = "JSONB")
    private String valuesJson = "{}";

    @Column(name = "snapshot_json", nullable = false, columnDefinition = "JSONB")
    private String snapshotJson;

    @Column(name = "process_instance_id")
    private String processInstanceId;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    public DocumentInstance() {}

    public DocumentInstance(UUID id, UUID documentId, String version, String folio, String snapshotJson, UUID createdBy) {
        super(id);
        this.documentId = documentId;
        this.version = version;
        this.folio = folio;
        this.snapshotJson = snapshotJson;
        this.createdBy = createdBy;
        this.status = InstanceStatus.DRAFT.value();
        this.createdAt = Instant.now();
    }

    public UUID getDocumentId() { return documentId; }
    public String getVersion() { return version; }
    public String getFolio() { return folio; }
    public String getStatus() { return status; }
    public String getValuesJson() { return valuesJson; }
    public String getSnapshotJson() { return snapshotJson; }
    public String getProcessInstanceId() { return processInstanceId; }
    public UUID getCreatedBy() { return createdBy; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getCompletedAt() { return completedAt; }

    public InstanceStatus getInstanceStatus() { return InstanceStatus.fromString(status); }

    public void setValuesJson(String valuesJson) { this.valuesJson = valuesJson; }
    public void setProcessInstanceId(String processInstanceId) { this.processInstanceId = processInstanceId; }

    public void submit() {
        this.status = InstanceStatus.SUBMITTED.value();
        registerEvent(new DocumentSubmittedEvent(UUID.randomUUID(), id, folio, Instant.now()));
    }

    public void complete() {
        this.status = InstanceStatus.COMPLETED.value();
        this.completedAt = Instant.now();
        registerEvent(new DocumentCompletedEvent(UUID.randomUUID(), id, folio, completedAt));
    }

    public void cancel() {
        this.status = InstanceStatus.CANCELLED.value();
    }
}
