package com.ilyanin.booking_platform.booking.domain.port.in;

import java.util.UUID;

import com.ilyanin.booking_platform.booking.domain.model.Booking;
import com.ilyanin.booking_platform.shared.DateRange;

public interface ChangeRequestUseCase {
    Booking changeRequest(UUID bookingId, DateRange newDateRange);
}
