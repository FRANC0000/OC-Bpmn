package com.bpmplatform.document.domain.entity;

import com.bpmplatform.common.domain.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "block_catalog_items")
public class BlockCatalogItem extends Entity<UUID> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "block_catalog_id", nullable = false)
    private BlockCatalog blockCatalog;

    @Column(nullable = false)
    private String label;

    @Column(nullable = false)
    private String type;

    @Column(columnDefinition = "JSONB")
    private String configJson;

    @Column(nullable = false)
    private int sortOrder;

    @Column(nullable = false)
    private boolean required;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    public BlockCatalogItem() {}

    public BlockCatalogItem(UUID id, String label, String type, String configJson, int sortOrder, boolean required) {
        super(id);
        this.label = label;
        this.type = type;
        this.configJson = configJson;
        this.sortOrder = sortOrder;
        this.required = required;
    }

    public BlockCatalog getBlockCatalog() { return blockCatalog; }
    public String getLabel() { return label; }
    public String getType() { return type; }
    public String getConfigJson() { return configJson; }
    public int getSortOrder() { return sortOrder; }
    public boolean isRequired() { return required; }
    public boolean isActive() { return isActive; }

    public void setBlockCatalog(BlockCatalog blockCatalog) { this.blockCatalog = blockCatalog; }
    public void setLabel(String label) { this.label = label; }
    public void setType(String type) { this.type = type; }
    public void setConfigJson(String configJson) { this.configJson = configJson; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
    public void setRequired(boolean required) { this.required = required; }
    public void deactivate() { this.isActive = false; }
}
