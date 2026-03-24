package com.ilyanin.booking_platform.booking.domain.event;

import java.util.UUID;

import com.ilyanin.booking_platform.shared.DateRange;
import com.ilyanin.booking_platform.shared.event.DomainEvent;

public class BookingChangeRequestedEvent extends DomainEvent {

    private final UUID guestId;
    private final DateRange dateRange;

    public BookingChangeRequestedEvent(
        UUID aggregateId, 
        UUID guestId,
        DateRange dateRange
    ) {
        super(aggregateId);
        this.guestId = guestId;
        this.dateRange = dateRange;
    }

    public UUID getGuestId() {
        return guestId;
    }

    public DateRange getDateRange() {
        return dateRange;
    }
}
