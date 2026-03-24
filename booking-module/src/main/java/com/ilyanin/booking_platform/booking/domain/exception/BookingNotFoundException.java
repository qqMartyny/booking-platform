package com.ilyanin.booking_platform.booking.domain.exception;

import java.util.UUID;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(UUID id) {
        super("Booking not found: " + id);
    }
}
