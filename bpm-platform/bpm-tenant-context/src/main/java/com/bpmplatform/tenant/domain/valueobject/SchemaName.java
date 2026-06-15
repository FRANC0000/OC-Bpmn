package com.bpmplatform.tenant.domain.valueobject;

import com.bpmplatform.common.domain.ValueObject;
import java.util.List;

public class SchemaName extends ValueObject {
    private final String value;

    public SchemaName(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Schema name must not be blank");
        if (!value.matches("^[a-z][a-z0-9_]*$")) throw new IllegalArgumentException("Invalid schema name format");
        this.value = value;
    }

    public String value() { return value; }

    @Override
    protected Iterable<Object> equalityComponents() {
        return List.of(value);
    }
}
