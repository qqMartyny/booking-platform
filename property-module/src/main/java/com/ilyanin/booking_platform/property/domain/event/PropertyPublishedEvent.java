package com.ilyanin.booking_platform.property.domain.event;

import java.util.UUID;

import com.ilyanin.booking_platform.property.domain.model.PropertyStatus;
import com.ilyanin.booking_platform.shared.event.DomainEvent;

public class PropertyPublishedEvent extends DomainEvent{

    private final UUID hostId;
    private final PropertyStatus status;
    
    public PropertyPublishedEvent(
        UUID aggregateId, 
        UUID hostId,
        PropertyStatus status
    ) {
        super(aggregateId);
        this.hostId = hostId;
        this.status = status;
    }

    public UUID getHostId() {
        return hostId;
    }

    public PropertyStatus getStatus() {
        return status;
    }
}
