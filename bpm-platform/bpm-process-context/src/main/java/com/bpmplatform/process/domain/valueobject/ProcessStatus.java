package com.bpmplatform.process.domain.valueobject;

public enum ProcessStatus {
    DRAFT("draft"),
    ACTIVE("active"),
    INACTIVE("inactive");

    private final String value;

    ProcessStatus(String value) { this.value = value; }

    public String value() { return value; }

    public static ProcessStatus fromString(String s) {
        for (var p : values()) if (p.value.equals(s)) return p;
        throw new IllegalArgumentException("Unknown process status: " + s);
    }
}
