package com.ilyanin.booking_platform.property.domain.port.in;

import java.util.UUID;

import com.ilyanin.booking_platform.property.domain.model.Property;
import com.ilyanin.booking_platform.shared.Money;

public interface SetPropertyPriceUseCase {
    Property update(
        UUID propertyId,
        Money pricePerNight
    );
}
