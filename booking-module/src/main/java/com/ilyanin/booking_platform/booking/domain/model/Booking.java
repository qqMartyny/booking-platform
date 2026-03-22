package com.ilyanin.booking_platform.booking.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ilyanin.booking_platform.booking.domain.event.BookingCreatedEvent;
import com.ilyanin.booking_platform.booking.domain.event.DomainEvent;
import com.ilyanin.booking_platform.shared.DateRange;
import com.ilyanin.booking_platform.shared.Money;

public class Booking {

    private final UUID id;
    private final UUID guestId;
    private final UUID roomId;
    private final DateRange dateRange;
    private final Money totalPrice;
    private final LocalDateTime createdAt;
    private BookingStatus status;
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private Booking(
        UUID id,
        UUID guestId,
        UUID roomId,
        DateRange dateRange,
        BookingStatus status,
        Money totalPrice,
        LocalDateTime createdAt
        ) {
        this.id = id;
        this.guestId = guestId;
        this.roomId = roomId;
        this.dateRange = dateRange;
        this.status = status;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }

    public static Booking create(
        UUID guestId, 
        UUID roomId, 
        DateRange dateRange, 
        Money totalPrice
    ) {

        UUID id = UUID.randomUUID();
        BookingStatus status = BookingStatus.PENDING;
        LocalDateTime createdAt = LocalDateTime.now();
        
        Booking booking = new Booking(
            id,
            guestId,
            roomId,
            dateRange,
            status,
            totalPrice,
            createdAt
        );

        booking.domainEvents.add(new BookingCreatedEvent());
        return booking;
    }

    
}
