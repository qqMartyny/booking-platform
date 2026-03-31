package com.ilyanin.booking_platform.property.domain.port.out;

import java.util.Optional;
import java.util.UUID;

import com.ilyanin.booking_platform.property.domain.model.Property;

public interface PropertyRepositoryPort {
    Property save(Property property);
    Optional<Property> findById(UUID id);
}
