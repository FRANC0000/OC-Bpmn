package com.bpmplatform.document.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "catalog_items")
public class CatalogItem extends com.bpmplatform.common.domain.Entity<UUID> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id", nullable = false)
    private Catalog catalog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CatalogItem parent;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String label;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "metadata_json", columnDefinition = "JSONB")
    private String metadataJson;

    public CatalogItem() {}

    public CatalogItem(UUID id, String code, String label, int sortOrder) {
        super(id);
        this.code = code;
        this.label = label;
        this.sortOrder = sortOrder;
    }

    public Catalog getCatalog() { return catalog; }
    public CatalogItem getParent() { return parent; }
    public String getCode() { return code; }
    public String getLabel() { return label; }
    public int getSortOrder() { return sortOrder; }
    public boolean isActive() { return isActive; }
    public String getMetadataJson() { return metadataJson; }

    void setCatalog(Catalog catalog) { this.catalog = catalog; }

    public void setParent(CatalogItem parent) { this.parent = parent; }
    public void setLabel(String label) { this.label = label; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
    public void setMetadataJson(String metadataJson) { this.metadataJson = metadataJson; }
    public void deactivate() { this.isActive = false; }
}
