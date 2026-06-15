package com.bpmplatform.document.domain.entity;

import com.bpmplatform.common.domain.Entity;
import com.bpmplatform.document.domain.valueobject.DocumentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@jakarta.persistence.Entity
@Table(name = "document_versions")
public class DocumentVersion extends Entity<UUID> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private DocumentDefinition documentDefinition;

    @Column(nullable = false)
    private String version;

    @Column(nullable = false)
    private String status = "draft";

    @Column(name = "blocks_json", nullable = false, columnDefinition = "JSONB")
    private String blocksJson = "[]";

    @Column(name = "metadata_json", nullable = false, columnDefinition = "JSONB")
    private String metadataJson = "[]";

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public DocumentVersion() {}

    public DocumentVersion(UUID id, String version, String blocksJson, String metadataJson, UUID createdBy) {
        super(id);
        this.version = version;
        this.blocksJson = blocksJson;
        this.metadataJson = metadataJson;
        this.createdBy = createdBy;
        this.status = DocumentStatus.DRAFT.value();
        this.createdAt = Instant.now();
    }

    public DocumentDefinition getDocumentDefinition() { return documentDefinition; }
    public String getVersion() { return version; }
    public String getStatus() { return status; }
    public String getBlocksJson() { return blocksJson; }
    public String getMetadataJson() { return metadataJson; }
    public UUID getCreatedBy() { return createdBy; }
    public Instant getCreatedAt() { return createdAt; }

    void setDocumentDefinition(DocumentDefinition doc) { this.documentDefinition = doc; }

    public void setBlocksJson(String blocksJson) { this.blocksJson = blocksJson; }
    public void setMetadataJson(String metadataJson) { this.metadataJson = metadataJson; }

    public void publish() { this.status = DocumentStatus.ACTIVE.value(); }
}
