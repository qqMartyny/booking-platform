package com.ilyanin.booking_platform.shared.event;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class DomainEvent {

    private final UUID eventId;
    private final UUID agregateId;
    private final LocalDateTime occuredAt;

    protected DomainEvent(
        UUID eventId, 
        UUID agregateId, 
        LocalDateTime occuredAt
    ) {
        this.eventId = eventId;
        this.agregateId = agregateId;
        this.occuredAt = occuredAt;
    }
}
