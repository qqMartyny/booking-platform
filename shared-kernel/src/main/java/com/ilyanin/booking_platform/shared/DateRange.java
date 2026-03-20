package com.ilyanin.booking_platform.shared;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record DateRange(
    LocalDate startDate,
    LocalDate endDate
){

    public DateRange {
        if (!startDate.isBefore(endDate)) {
            throw new IllegalArgumentException(
                "start date can only be before end date\n start date: " 
                + startDate + " end date: " + endDate
            );
        }
    }

    public boolean overlaps(DateRange other) {
        return startDate.isBefore(other.endDate) 
            && other.startDate.isBefore(endDate);
    }

    public boolean contains(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
            
    }

    public int nights() {
        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }

} 
