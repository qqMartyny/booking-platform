package com.ilyanin.booking_platform.property.domain.port.out;

import java.time.LocalDate;
import java.util.UUID;

import com.ilyanin.booking_platform.property.domain.model.ReservedDate;

public interface ReservedDateRepositoryPort {
    void save(ReservedDate reservedDate);
    void deleteByBookingId(UUID bookingId);
    boolean existsByPropertyIdAndDate(UUID propertyId, LocalDate date);
}
