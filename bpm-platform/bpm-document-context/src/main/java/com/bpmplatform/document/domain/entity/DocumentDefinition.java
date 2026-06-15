package com.bpmplatform.document.domain.entity;

import com.bpmplatform.common.domain.AggregateRoot;
import com.bpmplatform.document.domain.event.DocumentDefinitionCreatedEvent;
import com.bpmplatform.document.domain.valueobject.DocumentStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "document_definitions")
public class DocumentDefinition extends AggregateRoot<UUID> {

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String status = "draft";

    @OneToMany(mappedBy = "documentDefinition", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("createdAt DESC")
    private List<DocumentVersion> versions = new ArrayList<>();

    public DocumentDefinition() {}

    public DocumentDefinition(UUID id, String code, String name) {
        super(id);
        this.code = code;
        this.name = name;
        this.status = DocumentStatus.DRAFT.value();
        registerEvent(new DocumentDefinitionCreatedEvent(UUID.randomUUID(), id, code, name, Instant.now()));
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public List<DocumentVersion> getVersions() { return versions; }

    public DocumentStatus getDocumentStatus() { return DocumentStatus.fromString(status); }

    public void setDescription(String description) { this.description = description; }
    public void activate() { this.status = DocumentStatus.ACTIVE.value(); }

    public void addVersion(DocumentVersion version) {
        versions.add(version);
        version.setDocumentDefinition(this);
    }
}
