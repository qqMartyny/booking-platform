package com.ilyanin.booking_platform.property.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.ilyanin.booking_platform.shared.Money;

public record Availability(
    UUID id,
    UUID propertyId,
    LocalDate date,
    boolean available,
    Money price,
    String blockReason,
    LocalDateTime createdAt
) {
    
}
