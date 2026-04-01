package com.ilyanin.booking_platform.property.domain.port.in;

import com.ilyanin.booking_platform.shared.Money;

public record PropertySearchFilter(
    String city,
    String country,
    Money min,
    Money max,
    int page,
    int pageSize
) {
    
}
