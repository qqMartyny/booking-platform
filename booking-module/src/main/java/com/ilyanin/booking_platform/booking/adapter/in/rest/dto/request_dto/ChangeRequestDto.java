package com.ilyanin.booking_platform.booking.adapter.in.rest.dto.request_dto;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public record ChangeRequestDto(
    @NotNull @FutureOrPresent LocalDate newStartDate,
    @NotNull @FutureOrPresent LocalDate newEndDate
) {

}
