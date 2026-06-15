package com.bpmplatform.security.domain.valueobject;

import com.bpmplatform.common.domain.ValueObject;
import java.util.List;

public class PasswordHash extends ValueObject {
    private final String value;

    public PasswordHash(String value) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("Password hash must not be blank");
        this.value = value;
    }

    public String value() { return value; }

    @Override
    protected Iterable<Object> equalityComponents() {
        return List.of(value);
    }
}
