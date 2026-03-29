package com.ilyanin.booking_platform.property.domain.event;

import java.util.UUID;

import com.ilyanin.booking_platform.shared.event.DomainEvent;

public class PropertyInstantBookSetEvent extends DomainEvent{

    private final boolean instantBook;

    public PropertyInstantBookSetEvent(UUID aggregateId, boolean instantBook) {
        super(aggregateId);
        this.instantBook = instantBook;
    }

    public boolean isInstantBook() {
        return instantBook;
    }
}
