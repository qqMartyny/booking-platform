package com.ilyanin.booking_platform.booking.adapter.out.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ilyanin.booking_platform.booking.domain.model.BookingStatus;

import jakarta.persistence.LockModeType;

public interface BookingJpaRepository extends 
    JpaRepository<BookingJpaEntity, UUID>,
    JpaSpecificationExecutor<BookingJpaEntity> {
    
    @Query("""        
        SELECT b FROM BookingJpaEntity b
        WHERE b.roomId = :roomId
        AND :startDate < b.endDate
        AND b.startDate < :endDate
        AND :status = b.status
    """)
    List<BookingJpaEntity> findConflicting(
        @Param("roomId") UUID roomId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("status") BookingStatus status
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM BookingJpaRepository b WHERE b.id = :id")
    Optional<BookingJpaEntity> findByIdForUpdate(@Param("id") UUID id);
}
