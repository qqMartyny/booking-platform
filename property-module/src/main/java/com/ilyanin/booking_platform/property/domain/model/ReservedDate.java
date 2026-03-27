package com.ilyanin.booking_platform.property.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReservedDate(
    UUID id,
    UUID propertyId,
    LocalDate date,
    UUID bookingId,
    LocalDateTime createdAt
) {

}
