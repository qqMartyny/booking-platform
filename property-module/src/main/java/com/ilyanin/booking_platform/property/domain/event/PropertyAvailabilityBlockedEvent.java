package com.ilyanin.booking_platform.property.domain.event;

import java.util.UUID;

import com.ilyanin.booking_platform.shared.DateRange;
import com.ilyanin.booking_platform.shared.event.DomainEvent;

public class PropertyAvailabilityBlockedEvent extends DomainEvent{ 

    private final DateRange dateRange;
    private final String reason;
    
    public PropertyAvailabilityBlockedEvent(
        UUID aggregateId,
        DateRange dateRange,
        String reason
    ) {
        super(aggregateId);
        this.dateRange = dateRange;
        this.reason = reason;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public String getReason() {
        return reason;
    }
}
