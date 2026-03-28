package com.ilyanin.booking_platform.property.domain.event;

import java.util.UUID;

import com.ilyanin.booking_platform.property.domain.model.Address;
import com.ilyanin.booking_platform.shared.event.DomainEvent;

public class PropertyCreatedEvent extends DomainEvent {
    
    private final UUID hostId;
    private final String name;
    private final Address address;

    public PropertyCreatedEvent(
        UUID aggregateId,
        UUID hostId,
        String name,
        Address address
    ) {
        super(aggregateId);
        this.hostId = hostId;
        this.name = name;
        this.address = address;
    }

    public UUID getHostId() {
        return hostId;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }
}
