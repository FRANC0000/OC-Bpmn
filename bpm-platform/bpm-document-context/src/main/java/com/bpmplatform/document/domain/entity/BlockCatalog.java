package com.bpmplatform.document.domain.entity;

import com.bpmplatform.common.domain.AggregateRoot;
import com.bpmplatform.document.domain.event.BlockCatalogCreatedEvent;
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
@Table(name = "block_catalogs")
public class BlockCatalog extends AggregateRoot<UUID> {

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @OneToMany(mappedBy = "blockCatalog", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("sortOrder ASC")
    private List<BlockCatalogItem> items = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public BlockCatalog() {}

    public BlockCatalog(UUID id, String code, String name, String description) {
        super(id);
        this.code = code;
        this.name = name;
        this.description = description;
        this.createdAt = Instant.now();
        registerEvent(new BlockCatalogCreatedEvent(UUID.randomUUID(), id, code, name, createdAt));
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isActive() { return isActive; }
    public List<BlockCatalogItem> getItems() { return items; }
    public Instant getCreatedAt() { return createdAt; }

    public void setDescription(String description) { this.description = description; }
    public void deactivate() { this.isActive = false; }

    public void addItem(BlockCatalogItem item) {
        items.add(item);
        item.setBlockCatalog(this);
    }
}
