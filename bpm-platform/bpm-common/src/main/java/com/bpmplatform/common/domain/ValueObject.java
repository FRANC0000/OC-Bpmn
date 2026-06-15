package com.bpmplatform.common.domain;

public abstract class ValueObject {
    protected abstract Iterable<Object> equalityComponents();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValueObject other)) return false;
        var thisComponents = equalityComponents();
        var otherComponents = other.equalityComponents();
        var it1 = thisComponents.iterator();
        var it2 = otherComponents.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            var c1 = it1.next();
            var c2 = it2.next();
            if (!java.util.Objects.equals(c1, c2)) return false;
        }
        return !it1.hasNext() && !it2.hasNext();
    }

    @Override
    public int hashCode() {
        var components = equalityComponents();
        var result = 17;
        for (var c : components) {
            result = 31 * result + (c != null ? c.hashCode() : 0);
        }
        return result;
    }
}
