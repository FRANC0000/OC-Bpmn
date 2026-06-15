package com.bpmplatform.security.domain.valueobject;

public enum UserStatus {
    ACTIVE("active"),
    INACTIVE("inactive"),
    LOCKED("locked");

    private final String value;

    UserStatus(String value) { this.value = value; }

    public String value() { return value; }

    public static UserStatus fromString(String s) {
        for (var u : values()) if (u.value.equals(s)) return u;
        throw new IllegalArgumentException("Unknown user status: " + s);
    }
}
