package com.ilyanin.booking_platform.booking.adapter.in.rest;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ilyanin.booking_platform.booking.adapter.in.rest.dto.response_dto.ErrorResponse;
import com.ilyanin.booking_platform.booking.domain.exception.BookingNotFoundException;

@ControllerAdvice
public class BookingExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        var error = new ErrorResponse(
            "Internal server error", 
            e.getMessage(), 
            LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(error);
    }

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
    public ResponseEntity<ErrorResponse> handleGenericException(MethodArgumentNotValidException e) {
        var error = new ErrorResponse(
            "Validation error", 
            e.getMessage(), 
            LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(error);
    }
}
