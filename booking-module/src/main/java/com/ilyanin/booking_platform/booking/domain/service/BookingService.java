package com.ilyanin.booking_platform.booking.domain.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ilyanin.booking_platform.booking.domain.event.BookingApprovedEvent;
import com.ilyanin.booking_platform.booking.domain.model.Booking;
import com.ilyanin.booking_platform.booking.domain.model.BookingStatus;
import com.ilyanin.booking_platform.booking.domain.port.in.ApproveBookingUseCase;
import com.ilyanin.booking_platform.booking.domain.port.in.BookingSearchFilter;
import com.ilyanin.booking_platform.booking.domain.port.in.CancelBookingUseCase;
import com.ilyanin.booking_platform.booking.domain.port.in.ChangeRequestUseCase;
import com.ilyanin.booking_platform.booking.domain.port.in.CompleteBookingUseCase;
import com.ilyanin.booking_platform.booking.domain.port.in.CreateBookingUseCase;
import com.ilyanin.booking_platform.booking.domain.port.in.GetBookingUseCase;
import com.ilyanin.booking_platform.booking.domain.port.in.RejectBookingUseCase;
import com.ilyanin.booking_platform.booking.domain.port.in.SearchBookingsUseCase;
import com.ilyanin.booking_platform.booking.domain.port.out.BookingEventPublisherPort;
import com.ilyanin.booking_platform.booking.domain.port.out.BookingRepositoryPort;
import com.ilyanin.booking_platform.booking.domain.port.out.RoomAvailabilityPort;
import com.ilyanin.booking_platform.shared.DateRange;
import com.ilyanin.booking_platform.shared.Money;
import com.ilyanin.booking_platform.shared.PageResult;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BookingService implements
    ApproveBookingUseCase,
    CancelBookingUseCase,
    ChangeRequestUseCase,
    CompleteBookingUseCase,
    CreateBookingUseCase,
    GetBookingUseCase,
    RejectBookingUseCase,
    SearchBookingsUseCase{
    
    private final BookingRepositoryPort repository;
    private final BookingEventPublisherPort eventPublisher;
    private final RoomAvailabilityPort roomAvailability;

    public BookingService(
        BookingRepositoryPort repository, 
        BookingEventPublisherPort eventPublisher,
        RoomAvailabilityPort roomAvailability
    ) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.roomAvailability = roomAvailability;
    }

    public PageResult<Booking> searchBookings(BookingSearchFilter filter) {
        return repository.findAll(filter);
    }

    public Booking reject(UUID bookingId) {
        Booking booking = repository.findById(bookingId)
            .orElseThrow(
                () -> new EntityNotFoundException(
                    "Booking with id " + bookingId + " is not found"
                )
            );
        
        booking.reject();
        eventPublisher.publishAll(booking.pullDomainEvents());

        return booking;
    }

    public Booking get(UUID bookingId) {
        Booking booking = repository.findById(bookingId)
            .orElseThrow(
                () -> new EntityNotFoundException(
                    "Booking with id " + bookingId + " is not found"
                )
            ); 
        
        return booking;
    }

    public Booking create(
        UUID guestId, 
        UUID roomId, 
        DateRange dateRange, 
        Money totalPrice
    ) {
        if (!roomAvailability.isAvailable(roomId, dateRange)) {
            throw new IllegalStateException(
                "Room " + roomId + " is not available"
            );
        }

        var booking = Booking.create(guestId, roomId, dateRange, totalPrice);
        repository.save(booking);
        eventPublisher.publishAll(booking.pullDomainEvents());

        return booking;
    }

    public Booking complete(UUID bookingId) {
        Booking booking = repository.findById(bookingId)
            .orElseThrow(
                () -> new EntityNotFoundException(
                    "Booking with id " + bookingId + " is not found"
                )
            );
        
        booking.complete();
        eventPublisher.publishAll(booking.pullDomainEvents());

        return booking;
    }

    public Booking changeRequest(UUID bookingId, DateRange newDateRange) {
        Booking booking = repository.findById(bookingId)
            .orElseThrow(
                () -> new EntityNotFoundException(
                    "Booking with id " + bookingId + " is not found"
                )
            );
        
        if (!roomAvailability.isAvailable(booking.getRoomId(), booking.getDateRange())) {
            throw new IllegalStateException(
                "Room " + booking.getRoomId() 
                + " is not available while period " 
                + booking.getDateRange() 
            );
        }   
        
        booking.changeRequest();
        repository.save(booking);
        eventPublisher.publishAll(booking.pullDomainEvents());

        return booking;
    }

    public Booking cancel(UUID bookingId) {
        Booking booking = repository.findById(bookingId)
            .orElseThrow(
                () -> new EntityNotFoundException(
                    "Booking with id " + bookingId + " is not found"
                )
            );
        
        booking.cancel();
        eventPublisher.publishAll(booking.pullDomainEvents());

        return booking;
    }

    public Booking approve(UUID bookingId) {
        Booking booking = repository.findById(bookingId)
            .orElseThrow(
                () -> new EntityNotFoundException(
                    "Booking with id " + bookingId + " is not found"
                )
            );

        List<Booking> conflicts = repository.findConflicting(
            bookingId, 
            booking.getDateRange(), 
            BookingStatus.APPROVED);
        
        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException(
                "This booking is conflicting with: " + conflicts
            );
        }

        booking.approve();
        repository.save(booking);
        eventPublisher.publishAll(booking.pullDomainEvents());

        return booking;
    }
}
