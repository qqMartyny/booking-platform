package com.ilyanin.booking_platform.property.domain.event;

import java.util.UUID;

import com.ilyanin.booking_platform.shared.event.DomainEvent;

public class PropertyDescriptionSetEvent extends DomainEvent{

    private final String description;

    public PropertyDescriptionSetEvent(UUID aggregateId, String description) {
        super(aggregateId);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
