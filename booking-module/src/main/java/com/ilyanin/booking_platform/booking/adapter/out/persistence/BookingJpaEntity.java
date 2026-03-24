package com.ilyanin.booking_platform.booking.adapter.out.persistence;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

import com.ilyanin.booking_platform.booking.domain.model.BookingStatus;
import com.ilyanin.booking_platform.shared.DateRange;
import com.ilyanin.booking_platform.shared.Money;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name="bookings")
@Entity
public class BookingJpaEntity {
    
    @Id
    @Column(name="id")
    private UUID id;

    @Column(name="guest_id", nullable = false)
    private UUID guestId;

    @Column(name="room_id", nullable = false)
    private UUID roomId;

    @Column(name="start_date", nullable = false)
    private LocalDate startDate;

    @Column(name="end_date", nullable = false)
    private LocalDate endDate;

    @Column(name="amount", nullable = false)
    private BigDecimal amount;

    @Column(name="currency", nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private BookingStatus status;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    public BookingJpaEntity(
        UUID id, 
        UUID guestId, 
        UUID roomId, 
        DateRange dateRange, 
        Money money, 
        BookingStatus status,
        LocalDateTime createdAt
    ) {
        this.id = id;
        this.guestId = guestId;
        this.roomId = roomId;
        this.startDate = dateRange.startDate();
        this.endDate = dateRange.endDate();
        this.amount = money.amount();
        this.currency = money.currency();
        this.status = status;
        this.createdAt = createdAt;
    }

    protected BookingJpaEntity(){}

    public UUID getId() {
        return id;
    }

    public UUID getGuestId() {
        return guestId;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }  
}
