package com.ilyanin.booking_platform.shared.event;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class DomainEvent {

    private final UUID eventId;
    private final UUID agregateId;
    private final LocalDateTime occuredAt;

    protected DomainEvent(UUID agregateId) {
        this.eventId = UUID.randomUUID();
        this.agregateId = agregateId;
        this.occuredAt = LocalDateTime.now();
    }

    public UUID getEventId() {
        return eventId;
    }

    public UUID getAgregateId() {
        return agregateId;
    }

    public LocalDateTime getOccuredAt() {
        return occuredAt;
    }
}
