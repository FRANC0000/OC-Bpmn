package com.bpmplatform.tenant.domain.valueobject;

import com.bpmplatform.common.domain.ValueObject;
import java.util.List;

public class Slug extends ValueObject {
    private final String value;

    public Slug(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Slug must not be blank");
        if (!value.matches("^[a-z0-9]+(-[a-z0-9]+)*$")) throw new IllegalArgumentException("Invalid slug format");
        this.value = value.toLowerCase();
    }

    public String value() { return value; }

    @Override
    protected Iterable<Object> equalityComponents() {
        return List.of(value);
    }
}
