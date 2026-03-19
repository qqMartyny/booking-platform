package com.ilyanin.booking_platform.shared.event;

import java.time.LocalDateTime;
import java.util.UUID;

public interface DomainEvent {
    
    LocalDateTime occurredAt();
    UUID aggregateId();
    UUID eventId();
}
