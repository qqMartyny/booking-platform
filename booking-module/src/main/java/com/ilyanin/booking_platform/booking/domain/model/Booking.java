package com.ilyanin.booking_platform.booking.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ilyanin.booking_platform.booking.domain.event.BookingApprovedEvent;
import com.ilyanin.booking_platform.booking.domain.event.BookingCancelledEvent;
import com.ilyanin.booking_platform.booking.domain.event.BookingChangeRequestedEvent;
import com.ilyanin.booking_platform.booking.domain.event.BookingCompletedEvent;
import com.ilyanin.booking_platform.booking.domain.event.BookingCreatedEvent;
import com.ilyanin.booking_platform.booking.domain.event.BookingRejectedEvent;
import com.ilyanin.booking_platform.shared.DateRange;
import com.ilyanin.booking_platform.shared.Money;
import com.ilyanin.booking_platform.shared.event.DomainEvent;
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

    public void approve() {
        transitionTo(BookingStatus.APPROVED);
        domainEvents.add(new BookingApprovedEvent());
    }

    public void cancel() {
        transitionTo(BookingStatus.CANCELLED);
        domainEvents.add(new BookingCancelledEvent());
    }

    public void reject() {
        transitionTo(BookingStatus.REJECTED);
        domainEvents.add(new BookingRejectedEvent());
    }

    public void complete() {
        transitionTo(BookingStatus.COMPLETED);
        domainEvents.add(new BookingCompletedEvent());
    }

    public void changeRequest() {
        transitionTo(BookingStatus.CHANGE_REQUESTED);
        domainEvents.add(new BookingChangeRequestedEvent());
    }

    private void transitionTo(BookingStatus newStatus) {
        if (!status.canTransitionTo(newStatus)) {
            throw new IllegalStateException(
                "Cannot make transition from " + status + " to " + newStatus
            );
        }
        
        this.status = newStatus;
    }

    public UUID getId() {
        return id;
    }

    public UUID getGuestId() {
        return guestId;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public Money getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public BookingStatus getStatus() {
        return status;
    }
}
