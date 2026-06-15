package com.bpmplatform.common.domain;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.util.Objects;

@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class Entity<ID> {

    @Id
    private ID id;

    protected Entity() {}

    protected Entity(ID id) {
        this.id = Objects.requireNonNull(id, "id must not be null");
    }

    public ID getId() { return id; }
    protected void setId(ID id) { this.id = id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity<?> entity)) return false;
        return id != null && id.equals(entity.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}
