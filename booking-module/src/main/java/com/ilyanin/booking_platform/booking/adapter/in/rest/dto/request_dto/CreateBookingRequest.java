package com.ilyanin.booking_platform.booking.adapter.in.rest.dto.request_dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateBookingRequest(
    @NotNull UUID guestId,
    @NotNull UUID roomId,
    @NotNull @FutureOrPresent LocalDate startDate,
    @NotNull @FutureOrPresent LocalDate endDate,
    @NotNull @Positive BigDecimal amount,
    @NotBlank String currency
) {

}
