package com.ilyanin.booking_platform.booking.domain.port.out;

import java.util.List;

import com.ilyanin.booking_platform.shared.event.DomainEvent;

public interface BookingEventPublisherPort {
    void publish(DomainEvent event);
    void publishAll(List<DomainEvent> events);
}
