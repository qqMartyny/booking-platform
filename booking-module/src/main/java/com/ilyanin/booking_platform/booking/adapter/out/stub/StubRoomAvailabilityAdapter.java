package com.ilyanin.booking_platform.booking.adapter.out.stub;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.ilyanin.booking_platform.booking.domain.port.out.RoomAvailabilityPort;
import com.ilyanin.booking_platform.shared.DateRange;

@Component
public class StubRoomAvailabilityAdapter implements RoomAvailabilityPort {
    @Override
    public boolean isAvailable(UUID roomId, DateRange dateRange) {
        return true; // всегда доступна
    }
}
