package com.bpmplatform.process.domain.valueobject;

public enum VersionStatus {
    DRAFT("draft"),
    PUBLISHED("published"),
    DEPRECATED("deprecated");

    private final String value;

    VersionStatus(String value) { this.value = value; }

    public String value() { return value; }

    public static VersionStatus fromString(String s) {
        for (var v : values()) if (v.value.equals(s)) return v;
        throw new IllegalArgumentException("Unknown version status: " + s);
    }
}
