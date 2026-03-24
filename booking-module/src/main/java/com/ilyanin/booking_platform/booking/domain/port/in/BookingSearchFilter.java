package com.ilyanin.booking_platform.booking.domain.port.in;

import java.util.UUID;

import com.ilyanin.booking_platform.booking.domain.model.BookingStatus;

public record BookingSearchFilter(
    UUID guestId,
    UUID roomId,
    BookingStatus status,
    int page,
    int pageSize
) {

}
