package com.self.study.flashsale.flashsale.adapters.presenters;

import java.time.LocalDate;
import java.util.UUID;

import com.self.study.flashsale.flashsale.domain.models.Events;

public class EventResponse {

    private UUID id;
    private String name;
    private Integer totalCapacity;
    private Integer remainingCapacity;
    private LocalDate eventDate;

    public EventResponse(Events event) {
        this.id = event.getId();
        this.name = event.getName();
        this.totalCapacity = event.getTotalCapacity();
        this.remainingCapacity = event.getRemainingCapacity();
        this.eventDate = event.getEventDate();
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
}
