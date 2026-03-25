package com.ilyanin.booking_platform.booking.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ilyanin.booking_platform.booking.domain.model.Booking;
import com.ilyanin.booking_platform.booking.domain.model.BookingStatus;
import com.ilyanin.booking_platform.booking.domain.port.in.BookingSearchFilter;
import com.ilyanin.booking_platform.booking.domain.port.out.BookingRepositoryPort;
import com.ilyanin.booking_platform.shared.DateRange;
import com.ilyanin.booking_platform.shared.PageResult;

public class BookingRepositoryAdapter implements BookingRepositoryPort{

    private final BookingMapper mapper;
    private final BookingJpaRepository repository;

    public BookingRepositoryAdapter(BookingMapper mapper, BookingJpaRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public Booking save(Booking booking) {
        BookingJpaEntity entity = mapper.toEntity(booking);
        repository.save(entity);
        return booking;  
    }

    @Override
    public Optional<Booking> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Booking> findConflicting(UUID roomId, DateRange dateRange, BookingStatus status) {
        return repository.findConflicting(
            roomId, 
            dateRange.startDate(), 
            dateRange.endDate(), 
            status
        )
            .stream()
            .map(mapper::toDomain)
            .toList();
    }

    @Override
    public PageResult<Booking> findAll(BookingSearchFilter filter) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }
    
}
