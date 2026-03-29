package com.ilyanin.booking_platform.property.domain.event;

import java.util.UUID;

import com.ilyanin.booking_platform.shared.Money;
import com.ilyanin.booking_platform.shared.event.DomainEvent;

public class PropertyDetailsUpdatedEvent extends DomainEvent{

    private final UUID hostId;
    private final String name;
    private final String description;
    private final Money price;
    private final boolean instantBook;
    
    public PropertyDetailsUpdatedEvent(
        UUID aggregateId, 
        UUID hostId,
        String name,
        String description,
        Money price,
        boolean instantBook
    ) {
        super(aggregateId);
        this.hostId = hostId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.instantBook = instantBook;
    }

    public UUID getHostId() {
        return hostId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isInstantBook() {
        return instantBook;
    }

    public Money getPrice() {
        return price;
    }
}
