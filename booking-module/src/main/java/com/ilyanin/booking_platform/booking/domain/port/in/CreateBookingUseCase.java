package com.ilyanin.booking_platform.booking.domain.port.in;

import java.util.UUID;

import com.ilyanin.booking_platform.booking.domain.model.Booking;
import com.ilyanin.booking_platform.shared.DateRange;
import com.ilyanin.booking_platform.shared.Money;

public interface CreateBookingUseCase {
    Booking create(
        UUID guestId,
        UUID roomId,
        DateRange dateRange,
        Money totalPrice
    );
}
