package com.nerosoft.linkyou.seedwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Aggregate<TKey> {
    private TKey id;
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public TKey getId() {
        return id;
    }

    protected final void setId(TKey id) {
        this.id = Objects.requireNonNull(id, "Aggregate identity cannot be null");
    }

    public final List<DomainEvent> getEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    protected final void raiseEvent(DomainEvent event) {
        domainEvents.add(Objects.requireNonNull(event, "Domain event cannot be null"));
    }

    public final void clearEvents() {
        domainEvents.clear();
    }

    public final boolean sameIdentityAs(Aggregate<TKey> other) {
        return other != null && Objects.equals(this.id, other.id);
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Aggregate<?> other = (Aggregate<?>) obj;
        return id != null && Objects.equals(id, other.id);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getClass(), id);
    }
}
