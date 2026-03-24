package com.ilyanin.booking_platform.booking.domain.port.in;

import java.util.UUID;

import com.ilyanin.booking_platform.booking.domain.model.Booking;

public interface CompleteBookingUseCase {
    Booking complete(UUID bookingId);
}
