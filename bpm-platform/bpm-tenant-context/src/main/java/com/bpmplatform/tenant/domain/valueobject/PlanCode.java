package com.bpmplatform.tenant.domain.valueobject;

public enum PlanCode {
    BASIC("basic"),
    PROFESSIONAL("professional"),
    ENTERPRISE("enterprise");

    private final String code;

    PlanCode(String code) { this.code = code; }

    public String code() { return code; }

    public static PlanCode fromString(String s) {
        for (var p : values()) if (p.code.equals(s)) return p;
        throw new IllegalArgumentException("Unknown plan code: " + s);
    }
}
