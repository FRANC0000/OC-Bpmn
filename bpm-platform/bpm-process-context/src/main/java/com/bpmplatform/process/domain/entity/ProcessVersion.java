package com.bpmplatform.process.domain.entity;

import com.bpmplatform.common.domain.Entity;
import com.bpmplatform.process.domain.valueobject.VersionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@jakarta.persistence.Entity
@Table(name = "process_versions")
public class ProcessVersion extends Entity<UUID> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_id", nullable = false)
    private ProcessDefinition processDefinition;

    @Column(nullable = false)
    private String version;

    @Column(nullable = false)
    private String status = "draft";

    @Column(name = "bpmn_xml", nullable = false, columnDefinition = "TEXT")
    private String bpmnXml;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public ProcessVersion() {}

    public ProcessVersion(UUID id, String version, String bpmnXml, UUID createdBy) {
        super(id);
        this.version = version;
        this.bpmnXml = bpmnXml;
        this.createdBy = createdBy;
        this.status = VersionStatus.DRAFT.value();
        this.createdAt = Instant.now();
    }

    public ProcessDefinition getProcessDefinition() { return processDefinition; }
    public String getVersion() { return version; }
    public String getStatus() { return status; }
    public String getBpmnXml() { return bpmnXml; }
    public String getComment() { return comment; }
    public UUID getCreatedBy() { return createdBy; }
    public Instant getCreatedAt() { return createdAt; }

    public VersionStatus getVersionStatus() { return VersionStatus.fromString(status); }

    void setProcessDefinition(ProcessDefinition processDefinition) {
        this.processDefinition = processDefinition;
    }

    public void setComment(String comment) { this.comment = comment; }

    public void publish() {
        this.status = VersionStatus.PUBLISHED.value();
    }
}
