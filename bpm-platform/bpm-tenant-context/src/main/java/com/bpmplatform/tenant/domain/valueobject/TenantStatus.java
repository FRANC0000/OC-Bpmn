package com.bpmplatform.tenant.domain.valueobject;

public enum TenantStatus {
    ACTIVE("active"),
    SUSPENDED("suspended"),
    INACTIVE("inactive");

    private final String value;

    TenantStatus(String value) { this.value = value; }

    public String value() { return value; }

    public static TenantStatus fromString(String s) {
        for (var t : values()) if (t.value.equals(s)) return t;
        throw new IllegalArgumentException("Unknown tenant status: " + s);
    }
}
