package com.ilyanin.booking_platform.booking.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.ilyanin.booking_platform.booking.domain.model.Booking;
import com.ilyanin.booking_platform.booking.domain.model.BookingStatus;
import com.ilyanin.booking_platform.booking.domain.port.in.BookingSearchFilter;
import com.ilyanin.booking_platform.booking.domain.port.out.BookingRepositoryPort;
import com.ilyanin.booking_platform.shared.DateRange;
import com.ilyanin.booking_platform.shared.PageResult;

@Component
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

        Specification<BookingJpaEntity> spec = Specification.unrestricted();

        if (filter.guestId() != null) {
            spec = spec.and((root, query, cb) ->
                cb.equal(root.get("guestId"), filter.guestId())
            );
        }

        if (filter.roomId() != null) {
            spec = spec.and((root, query, cb) ->
                cb.equal(root.get("roomId"), filter.roomId())
            );
        }
        
        if (filter.status() != null) {
            spec = spec.and((root, query, cb) ->
                cb.equal(root.get("status"), filter.status())
            );
        }

        Pageable pageable = PageRequest.of(filter.page(), filter.pageSize());
        Page<BookingJpaEntity> page = repository.findAll(spec, pageable);

        List<Booking> content = page.getContent()
            .stream()
            .map(mapper::toDomain)
            .toList();

        return new PageResult<Booking>(
            content, 
            filter.page(), 
            filter.pageSize(), 
            page.getTotalElements());
    }

    @Override
    public Optional<Booking> findByIdForUpdate(UUID id) {
        return repository.findByIdForUpdate(id).map(mapper::toDomain);
    }
    
}
