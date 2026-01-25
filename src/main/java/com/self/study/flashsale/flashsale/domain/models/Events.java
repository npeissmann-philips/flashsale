package com.self.study.flashsale.flashsale.domain.models;

import java.time.LocalDate;
import java.util.UUID;

import com.self.study.flashsale.flashsale.drivers.db.entities.EventsEntity;

public class Events {
    private UUID id;
    private String name;
    private LocalDate eventDate;
    private int totalCapacity;
    private int remainingCapacity;

    public Events() {
    }

    public Events(UUID id, String name, LocalDate eventDate, int totalCapacity, int remainingCapacity) {
        this.id = id;
        this.name = name;
        this.eventDate = eventDate;
        this.totalCapacity = totalCapacity;
        this.remainingCapacity = remainingCapacity;
    }

    public Events(EventsEntity eventsEntity) {
        this.id = eventsEntity.getId();
        this.name = eventsEntity.getName();
        this.eventDate = eventsEntity.getEventDate();
        this.totalCapacity = eventsEntity.getTotalCapacity();
        this.remainingCapacity = eventsEntity.getRemainingCapacity();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public int getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(int totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public int getRemainingCapacity() {
        return remainingCapacity;
    }

    public void setRemainingCapacity(int remainingCapacity) {
        this.remainingCapacity = remainingCapacity;
    }

    public EventsEntity toEntity() {
        return new EventsEntity(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String name;
        private LocalDate eventDate;
        private int totalCapacity;
        private int remainingCapacity;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder eventDate(LocalDate eventDate) {
            this.eventDate = eventDate;
            return this;
        }

        public Builder totalCapacity(int totalCapacity) {
            this.totalCapacity = totalCapacity;
            return this;
        }

        public Builder remainingCapacity(int remainingCapacity) {
            this.remainingCapacity = remainingCapacity;
            return this;
        }

        public Events build() {
            return new Events(id, name, eventDate, totalCapacity, remainingCapacity);
        }
    }
}
