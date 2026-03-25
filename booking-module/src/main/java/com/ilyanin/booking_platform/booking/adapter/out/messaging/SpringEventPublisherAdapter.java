package com.ilyanin.booking_platform.booking.adapter.out.messaging;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.ilyanin.booking_platform.booking.domain.port.out.BookingEventPublisherPort;
import com.ilyanin.booking_platform.shared.event.DomainEvent;

@Component
public class SpringEventPublisherAdapter implements BookingEventPublisherPort{

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringEventPublisherAdapter(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(DomainEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishAll(List<DomainEvent> events) {
        events.forEach(this::publish);
    }
}
