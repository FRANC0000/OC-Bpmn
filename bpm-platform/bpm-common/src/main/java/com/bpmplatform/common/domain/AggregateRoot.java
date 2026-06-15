package com.bpmplatform.common.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class AggregateRoot<ID> {
    private final ID id;
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    protected AggregateRoot(ID id) {
        this.id = Objects.requireNonNull(id, "id must not be null");
    }

    public ID getId() { return id; }

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
        return id.equals(that.id);
    }

    @Override
    public int hashCode() { return id.hashCode(); }
}
