package com.ilyanin.booking_platform.shared.event;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class DomainEvent {

    private final UUID eventId;
    private final UUID aggregateId;
    private final LocalDateTime occurredAt;

    protected DomainEvent(UUID aggregateId) {
        this.eventId = UUID.randomUUID();
        this.aggregateId = aggregateId;
        this.occurredAt = LocalDateTime.now();
    }

    public UUID getEventId() {
        return eventId;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}
