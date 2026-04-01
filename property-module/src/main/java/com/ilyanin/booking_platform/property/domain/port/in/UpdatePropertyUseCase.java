package com.ilyanin.booking_platform.property.domain.port.in;

import java.util.UUID;

import com.ilyanin.booking_platform.property.domain.model.Property;
import com.ilyanin.booking_platform.shared.Money;

public interface UpdatePropertyUseCase {
    Property update(
        UUID propertyId,
        String name,
        String description,
        boolean instantBook,
        Money pricePerNight
    );
}
