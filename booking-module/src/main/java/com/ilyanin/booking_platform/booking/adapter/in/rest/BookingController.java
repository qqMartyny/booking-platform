package com.ilyanin.booking_platform.booking.adapter.in.rest;

import java.util.Currency;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ilyanin.booking_platform.booking.adapter.in.rest.dto.request_dto.ChangeRequestDto;
import com.ilyanin.booking_platform.booking.adapter.in.rest.dto.request_dto.CreateBookingRequest;
import com.ilyanin.booking_platform.booking.adapter.in.rest.dto.response_dto.BookingResponse;
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
import com.ilyanin.booking_platform.shared.DateRange;
import com.ilyanin.booking_platform.shared.Money;
import com.ilyanin.booking_platform.shared.PageResult;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    
    private final ApproveBookingUseCase approveBookingUseCase;
    private final CancelBookingUseCase cancelBookingUseCase;
    private final ChangeRequestUseCase changeRequestUseCase;
    private final CompleteBookingUseCase completeBookingUseCase;
    private final CreateBookingUseCase createBookingUseCase;
    private final GetBookingUseCase getBookingUseCase;
    private final RejectBookingUseCase rejectBookingUseCase;
    private final SearchBookingsUseCase searchBookingsUseCase;

    public BookingController(
        ApproveBookingUseCase approveBookingUseCase, 
        CancelBookingUseCase cancelBookingUseCase,
        ChangeRequestUseCase changeRequestUseCase, 
        CompleteBookingUseCase completeBookingUseCase,
        CreateBookingUseCase createBookingUseCase, 
        GetBookingUseCase getBookingUseCase,
        RejectBookingUseCase rejectBookingUseCase, 
        SearchBookingsUseCase searchBookingsUseCase
    ) {
        this.approveBookingUseCase = approveBookingUseCase;
        this.cancelBookingUseCase = cancelBookingUseCase;
        this.changeRequestUseCase = changeRequestUseCase;
        this.completeBookingUseCase = completeBookingUseCase;
        this.createBookingUseCase = createBookingUseCase;
        this.getBookingUseCase = getBookingUseCase;
        this.rejectBookingUseCase = rejectBookingUseCase;
        this.searchBookingsUseCase = searchBookingsUseCase;
    }

    private BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
            booking.getId(),
            booking.getGuestId(),
            booking.getRoomId(),
            booking.getDateRange().startDate(),
            booking.getDateRange().endDate(),
            booking.getStatus(),
            booking.getTotalPrice().amount(),
            booking.getTotalPrice().currency().getCurrencyCode(),
            booking.getCreatedAt()
        );
    }

    @PostMapping
    public ResponseEntity<BookingResponse> create(
        @Valid 
        @RequestBody
        CreateBookingRequest request
    ) {
        Booking booking = createBookingUseCase.create(
            request.guestId(),
            request.roomId(),
            new DateRange(request.startDate(), request.endDate()),
            new Money(request.amount(), Currency.getInstance(request.currency()))
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(booking));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> get(@PathVariable UUID id) {
        Booking booking = getBookingUseCase.get(id);
        return ResponseEntity.ok(toResponse(booking));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<BookingResponse> approve(@PathVariable UUID id) {
        Booking booking = approveBookingUseCase.approve(id);
        return ResponseEntity.ok(toResponse(booking));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<BookingResponse> reject(@PathVariable UUID id) {
        Booking booking = rejectBookingUseCase.reject(id);
        return ResponseEntity.ok(toResponse(booking));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancel(@PathVariable UUID id) {
        Booking booking = cancelBookingUseCase.cancel(id);
        return ResponseEntity.ok(toResponse(booking));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<BookingResponse> complete(@PathVariable UUID id) {
        Booking booking = completeBookingUseCase.complete(id);
        return ResponseEntity.ok(toResponse(booking));
    }

    @PostMapping("/{id}/change-request")
    public ResponseEntity<BookingResponse> changeRequest(
        @PathVariable UUID id,
        @Valid @RequestBody ChangeRequestDto request
    ) {
        Booking booking = changeRequestUseCase.changeRequest(
            id, 
            new DateRange(request.newStartDate(), request.newEndDate())
        );
        return ResponseEntity.ok(toResponse(booking));
    }

    @GetMapping
    public ResponseEntity<PageResult<BookingResponse>> search(
        @RequestParam(required = false) UUID guestId,
        @RequestParam(required = false) UUID roomId,
        @RequestParam(required = false) BookingStatus status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int pageSize
    ) {
        var filter = new BookingSearchFilter(
            guestId,
            roomId,
            status,
            page,
            pageSize
        );
        PageResult<Booking> bookings = searchBookingsUseCase
            .searchBookings(filter);
            
        List<BookingResponse> response = bookings.content()
            .stream()
            .map(this::toResponse)
            .toList();
        
        return ResponseEntity.ok(new PageResult<>(
            response,
            bookings.page(),
            bookings.pageSize(),
            bookings.totalElements()
        ));
    }
}
