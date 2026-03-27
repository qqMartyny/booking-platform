package com.ilyanin.booking_platform.property.domain.model;

public record Address(
    String street,
    String city,
    String postalCode,
    String country
) {

}
