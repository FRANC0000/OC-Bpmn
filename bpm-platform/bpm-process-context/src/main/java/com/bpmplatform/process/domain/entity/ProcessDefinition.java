package com.bpmplatform.process.domain.entity;

import com.bpmplatform.common.domain.AggregateRoot;
import com.bpmplatform.process.domain.event.ProcessCreatedEvent;
import com.bpmplatform.process.domain.valueobject.ProcessStatus;
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
@Table(name = "process_definitions")
public class ProcessDefinition extends AggregateRoot<UUID> {

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(name = "owner_user_id")
    private UUID ownerUserId;

    @Column(nullable = false)
    private String status = "draft";

    @OneToMany(mappedBy = "processDefinition", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("createdAt DESC")
    private List<ProcessVersion> versions = new ArrayList<>();

    public ProcessDefinition() {}

    public ProcessDefinition(UUID id, String name, String slug, UUID ownerUserId) {
        super(id);
        this.name = name;
        this.slug = slug;
        this.ownerUserId = ownerUserId;
        this.status = ProcessStatus.DRAFT.value();
        registerEvent(new ProcessCreatedEvent(UUID.randomUUID(), id, name, slug, Instant.now()));
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getSlug() { return slug; }
    public UUID getOwnerUserId() { return ownerUserId; }
    public String getStatus() { return status; }
    public List<ProcessVersion> getVersions() { return versions; }

    public ProcessStatus getProcessStatus() { return ProcessStatus.fromString(status); }

    public void setDescription(String description) { this.description = description; }

    public void activate() { this.status = ProcessStatus.ACTIVE.value(); }

    public void addVersion(ProcessVersion version) {
        versions.add(version);
        version.setProcessDefinition(this);
    }
}
