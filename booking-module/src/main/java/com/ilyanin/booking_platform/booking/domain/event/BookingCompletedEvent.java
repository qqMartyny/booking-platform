package com.ilyanin.booking_platform.booking.domain.event;

import java.util.UUID;

import com.ilyanin.booking_platform.shared.event.DomainEvent;

public class BookingCompletedEvent extends DomainEvent {

    public BookingCompletedEvent(UUID aggregateId) {
        super(aggregateId);
    }
}