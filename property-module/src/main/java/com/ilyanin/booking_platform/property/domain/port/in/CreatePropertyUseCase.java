package com.ilyanin.booking_platform.property.domain.port.in;

import java.util.UUID;

import com.ilyanin.booking_platform.property.domain.model.Address;
import com.ilyanin.booking_platform.property.domain.model.Property;

public interface CreatePropertyUseCase {
    Property create(UUID hostId, String name, Address address);
}
