package com.ilyanin.booking_platform.booking.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

import com.ilyanin.booking_platform.shared.event.DomainEvent;

public class BookingCompletedEvent implements DomainEvent{

    @Override
    public LocalDateTime occurredAt() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'occurredAt'");
    }

    @Override
    public UUID aggregateId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'aggregateId'");
    }

    @Override
    public UUID eventId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eventId'");
    }

}
