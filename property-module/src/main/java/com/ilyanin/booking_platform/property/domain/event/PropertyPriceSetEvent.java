package com.ilyanin.booking_platform.property.domain.event;

import java.util.UUID;

import com.ilyanin.booking_platform.shared.Money;
import com.ilyanin.booking_platform.shared.event.DomainEvent;

public class PropertyPriceSetEvent extends DomainEvent{

    private final Money price;

    public PropertyPriceSetEvent(UUID aggregateId, Money price) {
        super(aggregateId);
        this.price = price;
    }

    public Money getPrice() {
        return price;
    }

}
