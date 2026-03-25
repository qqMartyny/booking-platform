package com.ilyanin.booking_platform.booking.adapter.in.rest.dto.response_dto;

import java.time.LocalDateTime;

public record ErrorResponse(
    String message,
    String errorMessage,
    LocalDateTime errorTime
) {

}
