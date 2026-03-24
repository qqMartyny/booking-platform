package com.ilyanin.booking_platform.booking.domain.event;

import java.util.UUID;

import com.ilyanin.booking_platform.shared.event.DomainEvent;

public class BookingRejectedEvent extends DomainEvent {

    private final UUID guestId;

    public BookingRejectedEvent(
        UUID aggregateId, 
        UUID guestId
    ) {
        super(aggregateId);
        this.guestId = guestId;
    }

    public UUID getGuestId() {
        return guestId;
    }
}
