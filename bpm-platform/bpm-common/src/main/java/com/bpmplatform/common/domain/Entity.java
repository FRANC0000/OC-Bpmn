package com.bpmplatform.common.domain;

import java.util.Objects;

public abstract class Entity<ID> {
    private final ID id;

    protected Entity(ID id) {
        this.id = Objects.requireNonNull(id, "id must not be null");
    }

    public ID getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity<?> entity)) return false;
        return id.equals(entity.id);
    }

    @Override
    public int hashCode() { return id.hashCode(); }
}
