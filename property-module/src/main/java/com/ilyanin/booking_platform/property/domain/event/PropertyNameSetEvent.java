package com.ilyanin.booking_platform.property.domain.event;

import java.util.UUID;

import com.ilyanin.booking_platform.shared.event.DomainEvent;

public class PropertyNameSetEvent extends DomainEvent{

    private final String name;

    public PropertyNameSetEvent(UUID aggregateId, String name) {
        super(aggregateId);
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
