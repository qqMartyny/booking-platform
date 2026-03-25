package com.ilyanin.booking_platform.booking.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.ilyanin.booking_platform.booking.domain.event.BookingCreatedEvent;
import com.ilyanin.booking_platform.shared.DateRange;
import com.ilyanin.booking_platform.shared.Money;
import com.ilyanin.booking_platform.shared.event.DomainEvent;

public class BookingTest {

    private Booking createTestBooking() {
        return Booking.create(
            UUID.randomUUID(),
            UUID.randomUUID(),
            new DateRange(LocalDate.now().plusDays(1), LocalDate.now().plusDays(5)),
            new Money(BigDecimal.valueOf(500), Currency.getInstance("USD"))
        );
    }

    @Test
    void shouldCreateBookingWithPendingStatus() {

        var booking = createTestBooking();

        assertThat(booking.getStatus()).isEqualTo(BookingStatus.PENDING);
    }

    @Test
    void shouldGenerateEventWhenCreated() {

        var booking = createTestBooking();

        List<DomainEvent> events = booking.pullDomainEvents();

        assertThat(events).hasSize(1);
        assertThat(events.get(0)).isInstanceOf(BookingCreatedEvent.class);
    }

    @Test
    void shouldThrowExceptionWhenIllegalTransition() {

        var booking = createTestBooking();

        assertThatThrownBy(() -> booking.complete())    
            .isInstanceOf(IllegalStateException.class)
            .hasMessage(
                "Cannot make transition from " 
                + BookingStatus.PENDING 
                + " to " 
                + BookingStatus.COMPLETED
            );
    }

    @Test
    void shouldApproveBooking() {
        
        var booking = createTestBooking();

        booking.approve();

        assertThat(booking.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void shouldClearDomainEventList() {

        var booking = createTestBooking();

        booking.pullDomainEvents();

        List<DomainEvent> events = booking.pullDomainEvents();

        assertThat(events).isEmpty();
    }

    @Test
    void shouldCancelApprovedBooking() {

        var booking = createTestBooking();

        booking.approve();
        booking.cancel();

        assertThat(booking.getStatus()).isEqualTo(BookingStatus.CANCELLED);
    }
}
