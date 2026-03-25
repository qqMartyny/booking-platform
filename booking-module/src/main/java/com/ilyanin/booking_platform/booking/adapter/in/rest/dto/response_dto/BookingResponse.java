package com.ilyanin.booking_platform.booking.adapter.in.rest.dto.response_dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.ilyanin.booking_platform.booking.domain.model.BookingStatus;

public record BookingResponse(
    UUID id,
    UUID guestId,
    UUID roomId,
    LocalDate startDate,
    LocalDate endDate,
    BookingStatus status,
    BigDecimal amount,
    String currency,
    LocalDateTime createdAt
) {

}
