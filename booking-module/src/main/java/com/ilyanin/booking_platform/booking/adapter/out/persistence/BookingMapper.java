package com.ilyanin.booking_platform.booking.adapter.out.persistence;

import org.springframework.stereotype.Component;

import com.ilyanin.booking_platform.booking.domain.model.Booking;
import com.ilyanin.booking_platform.shared.DateRange;
import com.ilyanin.booking_platform.shared.Money;

@Component
public class BookingMapper {

    public BookingJpaEntity toEntity(Booking domain) {
        return new BookingJpaEntity(
            domain.getId(),
            domain.getGuestId(),
            domain.getRoomId(),
            domain.getDateRange(),
            domain.getTotalPrice(),
            domain.getStatus(),
            domain.getCreatedAt()
        );
    }

    public Booking toDomain(BookingJpaEntity entity) {
        
        DateRange dateRange = new DateRange(
            entity.getStartDate(), 
            entity.getEndDate()
        );
        Money totalPrice = new Money(entity.getAmount(), entity.getCurrency());
        return Booking.reconstitute(
            entity.getId(),
            entity.getGuestId(),
            entity.getRoomId(),
            dateRange,
            totalPrice,
            entity.getStatus(),
            entity.getCreatedAt()
        );
    }
}
