package com.ilyanin.booking_platform.property.domain.exception;

import java.util.UUID;

public class PropertyNotFoundException extends RuntimeException{
    public PropertyNotFoundException(UUID id) {
        super("Property with id=" + id + " is not found");
    }
}
