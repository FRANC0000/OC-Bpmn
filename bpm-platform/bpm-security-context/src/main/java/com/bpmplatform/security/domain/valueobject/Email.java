package com.bpmplatform.security.domain.valueobject;

import com.bpmplatform.common.domain.ValueObject;
import java.util.List;

public class Email extends ValueObject {
    private final String value;

    public Email(String value) {
        if (value == null || !value.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new IllegalArgumentException("Invalid email: " + value);
        this.value = value.toLowerCase();
    }

    public String value() { return value; }

    @Override
    protected Iterable<Object> equalityComponents() {
        return List.of(value);
    }
}
