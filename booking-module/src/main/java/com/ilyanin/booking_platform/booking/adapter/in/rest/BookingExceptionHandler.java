package com.ilyanin.booking_platform.booking.adapter.in.rest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ilyanin.booking_platform.booking.adapter.in.rest.dto.response_dto.ErrorResponse;
import com.ilyanin.booking_platform.booking.domain.exception.BookingNotFoundException;

@RestControllerAdvice
public class BookingExceptionHandler {

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookingNotFound(BookingNotFoundException e) {
        var error = new ErrorResponse(
            "Booking not found", 
            e.getMessage(), 
            LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleRoomConflict(IllegalArgumentException e) {
        var error = new ErrorResponse(
            "Room is conflicting", 
            e.getMessage(), 
            LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(error);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException e) {
        var error = new ErrorResponse(
            "Illegal state", 
            e.getMessage(), 
            LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        String details = e.getBindingResult().getFieldErrors()
            .stream()
            .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
            .collect(Collectors.joining(", "));
        
        var error = new ErrorResponse(
            "Validation error", 
            details, 
            LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        var error = new ErrorResponse(
            "Internal server error", 
            e.getMessage(), 
            LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(error);
    }
}
