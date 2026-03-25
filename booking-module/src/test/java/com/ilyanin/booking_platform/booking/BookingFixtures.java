package com.ilyanin.booking_platform.booking;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.UUID;

import com.ilyanin.booking_platform.booking.domain.model.Booking;
import com.ilyanin.booking_platform.shared.DateRange;
import com.ilyanin.booking_platform.shared.Money;

public class BookingFixtures {

    public static final UUID BOOKING_ID = UUID.randomUUID();
    public static final UUID GUEST_ID = UUID.randomUUID();
    public static final UUID ROOM_ID = UUID.randomUUID();
    
    public static DateRange defaultDateRange() {
        return new DateRange(
            LocalDate.now().plusDays(1),
            LocalDate.now().plusDays(5)
        );
    }

    public static Money defaultMoney() {
        return new Money(BigDecimal.valueOf(500), Currency.getInstance("USD"));
    }

    public static Booking defaultBooking() {
        return Booking.create(GUEST_ID, ROOM_ID, defaultDateRange(), defaultMoney());
    }
}
