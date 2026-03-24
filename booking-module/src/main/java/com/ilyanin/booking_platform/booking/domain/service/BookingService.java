package com.ilyanin.booking_platform.booking.domain.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ilyanin.booking_platform.booking.domain.exception.BookingNotFoundException;
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
                () -> new BookingNotFoundException(bookingId)
            );
        
        booking.reject();
        repository.save(booking);
        eventPublisher.publishAll(booking.pullDomainEvents());

        return booking;
    }

    public Booking get(UUID bookingId) {
        Booking booking = repository.findById(bookingId)
            .orElseThrow(
                () -> new BookingNotFoundException(bookingId)
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
                () -> new BookingNotFoundException(bookingId)
            );
        
        booking.complete();
        repository.save(booking);
        eventPublisher.publishAll(booking.pullDomainEvents());

        return booking;
    }

    public Booking changeRequest(UUID bookingId, DateRange newDateRange) {
        Booking booking = repository.findById(bookingId)
            .orElseThrow(
                () -> new BookingNotFoundException(bookingId)
            );
        
        if (!roomAvailability.isAvailable(booking.getRoomId(), newDateRange)) {
            throw new IllegalStateException(
                "Room " + booking.getRoomId() 
                + " is not available while period " 
                + booking.getDateRange() 
            );
        }   
        
        booking.changeRequest(newDateRange);
        repository.save(booking);
        eventPublisher.publishAll(booking.pullDomainEvents());

        return booking;
    }

    public Booking cancel(UUID bookingId) {
        Booking booking = repository.findById(bookingId)
            .orElseThrow(
                () -> new BookingNotFoundException(bookingId)
            );
        
        booking.cancel();
        repository.save(booking);
        eventPublisher.publishAll(booking.pullDomainEvents());

        return booking;
    }

    public Booking approve(UUID bookingId) {
        Booking booking = repository.findById(bookingId)
            .orElseThrow(
                () -> new BookingNotFoundException(bookingId)
            );

        List<Booking> conflicts = repository.findConflicting(
            booking.getRoomId(), 
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
