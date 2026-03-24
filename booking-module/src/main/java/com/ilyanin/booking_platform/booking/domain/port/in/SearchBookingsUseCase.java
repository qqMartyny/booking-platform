package com.ilyanin.booking_platform.booking.domain.port.in;

import com.ilyanin.booking_platform.booking.domain.model.Booking;
import com.ilyanin.booking_platform.shared.PageResult;

public interface SearchBookingsUseCase {
    PageResult<Booking> searchBookings(BookingSearchFilter filter);
}
