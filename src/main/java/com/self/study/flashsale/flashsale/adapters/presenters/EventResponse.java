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

    public EventResponse() {
    }

    public EventResponse(Events event) {
        this.id = event.getId();
        this.name = event.getName();
        this.totalCapacity = event.getTotalCapacity();
        this.remainingCapacity = event.getRemainingCapacity();
        this.eventDate = event.getEventDate();
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalCapacity(Integer totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public void setRemainingCapacity(Integer remainingCapacity) {
        this.remainingCapacity = remainingCapacity;
    }

    public void setEventDate(LocalDate eventDate) {
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
}
