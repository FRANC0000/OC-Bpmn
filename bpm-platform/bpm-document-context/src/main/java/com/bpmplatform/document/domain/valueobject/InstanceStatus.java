package com.bpmplatform.document.domain.valueobject;

public enum InstanceStatus {
    DRAFT("draft"),
    SUBMITTED("submitted"),
    COMPLETED("completed"),
    CANCELLED("cancelled");

    private final String value;

    InstanceStatus(String value) { this.value = value; }

    public String value() { return value; }

    public static InstanceStatus fromString(String s) {
        for (var i : values()) if (i.value.equals(s)) return i;
        throw new IllegalArgumentException("Unknown instance status: " + s);
    }
}
