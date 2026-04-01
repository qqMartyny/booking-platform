package com.ilyanin.booking_platform.property.domain.port.in;

import java.util.UUID;

import com.ilyanin.booking_platform.property.domain.model.Property;

public interface PublishPropertyUseCase {
    Property publish(UUID propertyId);
}
