package com.bpmplatform.document.domain.valueobject;

public enum BlockType {
    TEXT("text"),
    INPUT("input"),
    TEXTAREA("textarea"),
    SELECT("select"),
    DATE("date"),
    GRID("grid"),
    FILE("file"),
    SIGNATURE("signature"),
    SECTION("section");

    private final String value;

    BlockType(String value) { this.value = value; }

    public String value() { return value; }

    public static BlockType fromString(String s) {
        for (var b : values()) if (b.value.equals(s)) return b;
        throw new IllegalArgumentException("Unknown block type: " + s);
    }
}
