package com.ilyanin.booking_platform.booking.domain.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ilyanin.booking_platform.booking.BookingFixtures;
import com.ilyanin.booking_platform.booking.domain.exception.BookingNotFoundException;
import com.ilyanin.booking_platform.booking.domain.model.Booking;
import com.ilyanin.booking_platform.booking.domain.port.out.BookingEventPublisherPort;
import com.ilyanin.booking_platform.booking.domain.port.out.BookingRepositoryPort;
import com.ilyanin.booking_platform.booking.domain.port.out.RoomAvailabilityPort;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    
    @Mock
    private BookingRepositoryPort repository;

    @Mock 
    private BookingEventPublisherPort eventPublisher;

    @Mock 
    private RoomAvailabilityPort roomAvailability;

    @InjectMocks
    private BookingService service;

    @Test
    void shouldThrowRoomIsNotAvailableWhenCreate() {
        when(roomAvailability.isAvailable(any(), any())).thenReturn(false);
        assertThatThrownBy(() -> service.create(
            BookingFixtures.GUEST_ID,
            BookingFixtures.ROOM_ID,
            BookingFixtures.defaultDateRange(),
            BookingFixtures.defaultMoney()
        ))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Room " + BookingFixtures.ROOM_ID + " is not available");
    }

    @Test
    void shouldCallSaveAndPublishWhenCreated() {

        when(roomAvailability.isAvailable(any(), any())).thenReturn(true);

        service.create(
            BookingFixtures.GUEST_ID,
            BookingFixtures.ROOM_ID,
            BookingFixtures.defaultDateRange(),
            BookingFixtures.defaultMoney()
        );

        verify(repository).save(any());
        verify(eventPublisher).publishAll(any());
    }

    @Test
    void shouldThrowBookingNotFound() {
        when(repository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.get(BookingFixtures.BOOKING_ID))
            .isInstanceOf(BookingNotFoundException.class);
    }

    @Test
    void shouldCallFindConflicting() {
        when(repository.findByIdForUpdate(any())).thenReturn(Optional.of(BookingFixtures.defaultBooking()));
        when(repository.findConflicting(any(), any(), any())).thenReturn(new ArrayList<Booking>());
        service.approve(BookingFixtures.BOOKING_ID);
        verify(repository).findConflicting(any(), any(), any());  

    }
}
