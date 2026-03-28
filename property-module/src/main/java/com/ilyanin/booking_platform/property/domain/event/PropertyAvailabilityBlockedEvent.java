package com.ilyanin.booking_platform.property.domain.event;

import java.util.UUID;

import com.ilyanin.booking_platform.shared.event.DomainEvent;

public class PropertyAvailabilityBlockedEvent extends DomainEvent{

    public PropertyAvailabilityBlockedEvent(UUID aggregateId) {
        super(aggregateId);
        //TODO Auto-generated constructor stub
    }

}
