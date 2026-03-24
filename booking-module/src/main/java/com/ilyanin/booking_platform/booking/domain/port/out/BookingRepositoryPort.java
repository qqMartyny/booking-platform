package com.ilyanin.booking_platform.booking.domain.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ilyanin.booking_platform.booking.domain.model.Booking;
import com.ilyanin.booking_platform.booking.domain.port.in.BookingSearchFilter;
import com.ilyanin.booking_platform.shared.DateRange;
import com.ilyanin.booking_platform.shared.PageResult;

public interface BookingRepositoryPort {
    Booking save(Booking booking);
    Optional<Booking> findById(UUID id);
    List<Booking> findConflicting(UUID roomId, DateRange dateRange);
    PageResult<Booking> findAll(BookingSearchFilter filter);
}
