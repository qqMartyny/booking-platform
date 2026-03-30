package com.ilyanin.booking_platform.property.domain.exception;

public class PropertyNotReadyToPublishException extends RuntimeException {
    public PropertyNotReadyToPublishException() {
        super("Property must have name, description, and price to be published");
    }
}
