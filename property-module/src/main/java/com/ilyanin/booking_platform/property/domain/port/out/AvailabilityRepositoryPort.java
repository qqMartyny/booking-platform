package com.ilyanin.booking_platform.property.domain.port.out;

import java.util.List;
import java.util.UUID;

import com.ilyanin.booking_platform.property.domain.model.Availability;
import com.ilyanin.booking_platform.shared.DateRange;

public interface AvailabilityRepositoryPort {
    void save(Availability availability);
    List<Availability> findByPropertyIdAndDateRange(UUID id, DateRange dateRange);
}
