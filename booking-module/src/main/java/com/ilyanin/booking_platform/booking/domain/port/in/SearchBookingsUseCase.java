package com.ilyanin.booking_platform.booking.domain.port.in;

import com.ilyanin.booking_platform.booking.domain.model.Booking;
import com.ilyanin.booking_platform.booking.domain.service.BookingSearchFilter;

public interface SearchBookingsUseCase {
    Booking searchBookings(BookingSearchFilter filter);
}
