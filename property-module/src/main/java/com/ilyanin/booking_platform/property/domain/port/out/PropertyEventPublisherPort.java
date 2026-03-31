package com.ilyanin.booking_platform.property.domain.port.out;

import java.util.List;

import com.ilyanin.booking_platform.shared.event.DomainEvent;

public interface PropertyEventPublisherPort {
    void publish(DomainEvent event);
    void publishAll(List<DomainEvent> events);
}
