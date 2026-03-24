package com.ilyanin.booking_platform.booking.domain.port.out;

import java.util.UUID;

import com.ilyanin.booking_platform.shared.DateRange;

public interface RoomAvailabilityPort {
    boolean isAvailable(UUID roomId, DateRange dateRange);
}
