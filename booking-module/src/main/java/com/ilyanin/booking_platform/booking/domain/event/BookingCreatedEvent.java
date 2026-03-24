package com.ilyanin.booking_platform.booking.domain.event;

import java.util.UUID;

import com.ilyanin.booking_platform.shared.DateRange;
import com.ilyanin.booking_platform.shared.Money;
import com.ilyanin.booking_platform.shared.event.DomainEvent;

public class BookingCreatedEvent extends DomainEvent {

    private final UUID guestId;
    private final UUID roomId;
    private final DateRange dateRange;
    private final Money totalPrice;

    public BookingCreatedEvent(
        UUID aggregateId, 
        UUID guestId,
        UUID roomId, 
        DateRange dateRange, 
        Money totalPrice
    ) {
        super(aggregateId);
        this.guestId = guestId;
        this.roomId = roomId;
        this.dateRange = dateRange;
        this.totalPrice = totalPrice;
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

    public Money getTotalPrice() {
        return totalPrice;
    }
}