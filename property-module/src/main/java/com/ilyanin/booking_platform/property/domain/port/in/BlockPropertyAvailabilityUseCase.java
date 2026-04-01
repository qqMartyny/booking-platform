package com.ilyanin.booking_platform.property.domain.port.in;

import java.util.UUID;

import com.ilyanin.booking_platform.shared.DateRange;

public interface BlockPropertyAvailabilityUseCase {
    void blockProperty(UUID propertyId, DateRange dateRange, String reason);
}
