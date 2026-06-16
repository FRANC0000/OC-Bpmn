package com.bpmplatform.process.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "process_templates")
public class ProcessTemplate extends com.bpmplatform.common.domain.Entity<UUID> {

    @Column(nullable = false)
    private String name;

    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "bpmn_xml", nullable = false, columnDefinition = "TEXT")
    private String bpmnXml;

    @Column(name = "config_json", columnDefinition = "JSONB")
    private String configJson;

    @Column(nullable = false)
    private String version = "1.0.0";

    @Column(name = "is_published", nullable = false)
    private boolean isPublished = false;

    public ProcessTemplate() {}

    public ProcessTemplate(UUID id, String name, String bpmnXml) {
        super(id);
        this.name = name;
        this.bpmnXml = bpmnXml;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getBpmnXml() { return bpmnXml; }
    public String getConfigJson() { return configJson; }
    public String getVersion() { return version; }
    public boolean isPublished() { return isPublished; }

    public void setCategory(String category) { this.category = category; }
    public void setDescription(String description) { this.description = description; }
    public void setConfigJson(String configJson) { this.configJson = configJson; }
    public void publish() { this.isPublished = true; }
}
