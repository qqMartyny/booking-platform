package com.ilyanin.booking_platform.property.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ilyanin.booking_platform.property.domain.event.PropertyArchivedEvent;
import com.ilyanin.booking_platform.property.domain.event.PropertyCreatedEvent;
import com.ilyanin.booking_platform.property.domain.event.PropertyDescriptionSetEvent;
import com.ilyanin.booking_platform.property.domain.event.PropertyDetailsUpdatedEvent;
import com.ilyanin.booking_platform.property.domain.event.PropertyDraftedEvent;
import com.ilyanin.booking_platform.property.domain.event.PropertyInstantBookSetEvent;
import com.ilyanin.booking_platform.property.domain.event.PropertyNameSetEvent;
import com.ilyanin.booking_platform.property.domain.event.PropertyPriceSetEvent;
import com.ilyanin.booking_platform.property.domain.event.PropertyPublishedEvent;
import com.ilyanin.booking_platform.shared.Money;
import com.ilyanin.booking_platform.shared.event.DomainEvent;

public class Property {

    private final UUID id;
    private final UUID hostId;
    private String name;
    private String description;
    private final Address address;
    private Money pricePerNight;
    private boolean instantBook;
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

    public static Property reconstitute(
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
        return new Property(
            id, hostId, name, description, address,
            pricePerNight, instantBook, status, createdAt, updatedAt
        );
    }

    public static Property create(UUID hostId, String name, Address address) {

        validateHostId(hostId);
        validateName(name);
        validateAddress(address);

        var id = UUID.randomUUID();
        String description = null;
        Money pricePerNight = null;
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

    public void draft() {
        transitionTo(PropertyStatus.DRAFT);
        this.updatedAt = LocalDateTime.now();
        domainEvents.add(new PropertyDraftedEvent(
            id,
            hostId,
            status
        ));
    }

    public void publish() {
        if (!readyToPublish()) {
            throw new IllegalArgumentException(
                "The property is not ready to be published"
            );
        }
        transitionTo(PropertyStatus.PUBLISHED);
        this.updatedAt = LocalDateTime.now();
        domainEvents.add(new PropertyPublishedEvent(
            id,
            hostId,
            status
        ));
    } 

    private boolean readyToPublish() {
        return 
            name != null
            && !name.isBlank()
            && description != null
            && !description.isBlank()
            && pricePerNight != null;
    }

    public void archive() {
        transitionTo(PropertyStatus.ARCHIVED);
        this.updatedAt = LocalDateTime.now();
        domainEvents.add(new PropertyArchivedEvent(
            id,
            hostId,
            status
        ));
    }

    private void transitionTo(PropertyStatus newStatus) {
        if(!status.canTransitionTo(newStatus)) {
            throw new IllegalStateException(
                "Cannot transition from " + status + " to " + newStatus
            );
        }
        this.status = newStatus;
    }

    public void updateDetails(
        String newName,
        String newDescription,
        boolean newInstantBook,
        Money newPrice
    ) {
        if (status != (PropertyStatus.DRAFT)) {
            throw new IllegalStateException("Draft property before update");
        }

        validateName(newName);
        validateDescription(newDescription);
        validatePrice(newPrice);

        this.name = newName;
        this.description = newDescription;
        this.instantBook = newInstantBook;
        this.pricePerNight = newPrice;
        this.updatedAt = LocalDateTime.now();

        domainEvents.add(new PropertyDetailsUpdatedEvent(
            id,
            hostId,
            name,
            description,
            pricePerNight,
            instantBook
        ));
    }

    public void setPrice(Money newPrice) {
        validatePrice(newPrice);
        this.pricePerNight = newPrice;
        this.updatedAt = LocalDateTime.now();
        domainEvents.add(new PropertyPriceSetEvent(id, pricePerNight));
    }

    public void setName(String newName) {
        validateName(newName);
        this.name = newName;
        this.updatedAt = LocalDateTime.now();
        domainEvents.add(new PropertyNameSetEvent(id, name));
    }

    public void setDescription(String newDescription) {
        validateDescription(newDescription);
        this.description = newDescription;
        this.updatedAt = LocalDateTime.now();
        domainEvents.add(new PropertyDescriptionSetEvent(id, description));
    }

    public void setInstantBook(boolean newInstantBook) {
        this.instantBook = newInstantBook;
        this.updatedAt = LocalDateTime.now();
        domainEvents.add(new PropertyInstantBookSetEvent(id, instantBook));
    }

    private static void validateHostId(UUID hostId) {
        if (hostId == null) {
            throw new IllegalArgumentException("Host ID cannot be null");
        }
    }
    
    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Property name cannot be empty");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Property name too long (max 255)");
        }
    }

    private static void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Property description cannot be empty");
        }
    }
    
    private static void validateAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Property address cannot be null");
        }
    }
    
    private static void validatePrice(Money price) {
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
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
