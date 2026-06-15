package com.bpmplatform.security.domain.valueobject;

public enum RoleType {
    PRIMARY("primary"),
    SECONDARY("secondary");

    private final String value;

    RoleType(String value) { this.value = value; }

    public String value() { return value; }

    public static RoleType fromString(String s) {
        for (var r : values()) if (r.value.equals(s)) return r;
        throw new IllegalArgumentException("Unknown role type: " + s);
    }
}
