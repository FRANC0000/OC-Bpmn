package com.bpmplatform.common.domain;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class AggregateRoot<ID> {

    @Id
    private ID id;

    @Transient
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    protected AggregateRoot() {}

    protected AggregateRoot(ID id) {
        this.id = Objects.requireNonNull(id, "id must not be null");
    }

    public ID getId() { return id; }
    protected void setId(ID id) { this.id = id; }

    protected void registerEvent(DomainEvent event) {
        domainEvents.add(Objects.requireNonNull(event));
    }

    public List<DomainEvent> clearEvents() {
        var events = List.copyOf(domainEvents);
        domainEvents.clear();
        return events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AggregateRoot<?> that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}
