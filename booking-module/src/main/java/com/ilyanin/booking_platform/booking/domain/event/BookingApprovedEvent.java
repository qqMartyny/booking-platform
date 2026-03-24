package com.ilyanin.booking_platform.booking.domain.event;

import java.util.UUID;

import com.ilyanin.booking_platform.shared.DateRange;
import com.ilyanin.booking_platform.shared.event.DomainEvent;

public class BookingApprovedEvent extends DomainEvent {

    private final UUID guestId;
    private final UUID roomId;
    private final DateRange dateRange;

    public BookingApprovedEvent(
        UUID aggregateId, 
        UUID guestId,
        UUID roomId, 
        DateRange dateRange
    ) {
        super(aggregateId);
        this.guestId = guestId;
        this.roomId = roomId;
        this.dateRange = dateRange;
    }

    public UUID getGuestId() {
        return guestId;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public DateRange getDateRange() {
        return dateRange;
    }
}
