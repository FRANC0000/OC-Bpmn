package com.bpmplatform.document.domain.valueobject;

public enum DocumentStatus {
    DRAFT("draft"),
    ACTIVE("active"),
    INACTIVE("inactive");

    private final String value;

    DocumentStatus(String value) { this.value = value; }

    public String value() { return value; }

    public static DocumentStatus fromString(String s) {
        for (var d : values()) if (d.value.equals(s)) return d;
        throw new IllegalArgumentException("Unknown document status: " + s);
    }
}
