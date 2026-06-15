package com.bpmplatform.document.domain.entity;

import com.bpmplatform.common.domain.AggregateRoot;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "catalogs")
public class Catalog extends AggregateRoot<UUID> {

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String level = "tenant";

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CatalogItem> items = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public Catalog() {}

    public Catalog(UUID id, String code, String name, String description) {
        super(id);
        this.code = code;
        this.name = name;
        this.description = description;
        this.createdAt = Instant.now();
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getLevel() { return level; }
    public boolean isActive() { return isActive; }
    public List<CatalogItem> getItems() { return items; }
    public Instant getCreatedAt() { return createdAt; }

    public void setDescription(String description) { this.description = description; }
    public void setLevel(String level) { this.level = level; }
    public void deactivate() { this.isActive = false; }

    public void addItem(CatalogItem item) {
        items.add(item);
        item.setCatalog(this);
    }
}
