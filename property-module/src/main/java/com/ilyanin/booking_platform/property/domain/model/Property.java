package com.ilyanin.booking_platform.property.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ilyanin.booking_platform.property.domain.event.PropertyCreatedEvent;
import com.ilyanin.booking_platform.shared.Money;
import com.ilyanin.booking_platform.shared.event.DomainEvent;

public class Property {

    private final UUID id;
    private final UUID hostId;
    private String name;
    private String description;
    private final Address address;
    private Money pricePerNight;
    private final boolean instantBook;
    private PropertyStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private final List<DomainEvent> domainEvents = new ArrayList<>();
    
    private Property(
        UUID id, 
        UUID hostId, 
        String name, 
        String description, 
        Address address, 
        Money pricePerNight,
        boolean instantBook, 
        PropertyStatus status, 
        LocalDateTime createdAt, 
        LocalDateTime updatedAt
    ) {
        this.id = id;
        this.hostId = hostId;
        this.name = name;
        this.description = description;
        this.address = address;
        this.pricePerNight = pricePerNight;
        this.instantBook = instantBook;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Property create(UUID hostId, String name, Address address) {

        if (hostId == null) {
            throw new IllegalArgumentException("Host id cannot be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }

        var id = UUID.randomUUID();
        var description = "Default description";
        var pricePerNight = Money.defaultMoney();
        var instantBook = false;
        var status = PropertyStatus.DRAFT;
        var createdAt = LocalDateTime.now();
        var updatedAt = LocalDateTime.now();
        var property = new Property(
            id,
            hostId,
            name,
            description,
            address,
            pricePerNight,
            instantBook,
            status,
            createdAt,
            updatedAt
        );
        property.domainEvents.add(new PropertyCreatedEvent(id, hostId, name, address));
        return property;
    }




    public UUID getId() {
        return id;
    }

    public UUID getHostId() {
        return hostId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Address getAddress() {
        return address;
    }

    public Money getPricePerNight() {
        return pricePerNight;
    }

    public boolean isInstantBook() {
        return instantBook;
    }

    public PropertyStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = List.copyOf(domainEvents);
        domainEvents.clear();
        return events;
    }
}
