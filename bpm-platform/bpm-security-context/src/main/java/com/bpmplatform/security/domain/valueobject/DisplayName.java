package com.bpmplatform.security.domain.valueobject;

import com.bpmplatform.common.domain.ValueObject;
import java.util.List;

public class DisplayName extends ValueObject {
    private final String value;

    public DisplayName(String value) {
        if (value == null || value.isBlank() || value.length() > 255)
            throw new IllegalArgumentException("Display name must be between 1 and 255 characters");
        this.value = value.trim();
    }

    public String value() { return value; }

    @Override
    protected Iterable<Object> equalityComponents() {
        return List.of(value);
    }
}
