package com.bpmplatform.document.domain.valueobject;

public enum ValidationRule {
    REQUIRED("required"),
    MIN_LENGTH("minLength"),
    MAX_LENGTH("maxLength"),
    MIN_VALUE("minValue"),
    MAX_VALUE("maxValue"),
    PATTERN("pattern"),
    EMAIL("email");

    private final String value;

    ValidationRule(String value) { this.value = value; }

    public String value() { return value; }

    public static ValidationRule fromString(String s) {
        for (var r : values()) if (r.value.equals(s)) return r;
        throw new IllegalArgumentException("Unknown validation rule: " + s);
    }
}
