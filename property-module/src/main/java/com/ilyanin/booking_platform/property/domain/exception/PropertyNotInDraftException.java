package com.ilyanin.booking_platform.property.domain.exception;

import com.ilyanin.booking_platform.property.domain.model.PropertyStatus;

public class PropertyNotInDraftException extends RuntimeException {
    public PropertyNotInDraftException(PropertyStatus currentStatus) {
        super("Property must be in DRAFT status, but is " + currentStatus);
    }
}
