package com.nerosoft.linkyou.seedwork;

public abstract class DomainEvent {
    private final String id = java.util.UUID.randomUUID().toString();
    private final java.time.Instant occurredAt = java.time.Instant.now();

    public String getId() {
        return id;
    }

    public java.time.Instant getOccurredAt() {
        return occurredAt;
    }
}
