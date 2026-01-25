package com.self.study.flashsale.flashsale.adapters.presenters;

import java.time.LocalDate;
import java.util.UUID;

import com.self.study.flashsale.flashsale.domain.models.Events;

public class EventRequest {
    private UUID id;
    private String name;
    private Integer totalCapacity;
    private Integer remainingCapacity;
    private LocalDate eventDate;

    public EventRequest() {
    }

    public EventRequest(UUID id, String name, Integer totalCapacity, Integer remainingCapacity, LocalDate eventDate) {
        this.id = id;
        this.name = name;
        this.totalCapacity = totalCapacity;
        this.remainingCapacity = remainingCapacity;
        this.eventDate = eventDate;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getTotalCapacity() {
        return totalCapacity;
    }

    public Integer getRemainingCapacity() {
        return remainingCapacity;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public Events toDomain() {
        return Events.builder()
                .id(id)
                .name(name)
                .totalCapacity(totalCapacity)
                .remainingCapacity(remainingCapacity)
                .eventDate(eventDate)
                .build();
    }

}
