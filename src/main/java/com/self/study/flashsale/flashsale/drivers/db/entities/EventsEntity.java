package com.self.study.flashsale.flashsale.drivers.db.entities;

import java.time.LocalDate;
import java.util.UUID;

import com.self.study.flashsale.flashsale.domain.models.Events;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "events")
public class EventsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private LocalDate eventDate;
    private int totalCapacity;
    private int remainingCapacity;

    public EventsEntity() {
    }

    public EventsEntity(Events events) {
        this.id = events.getId();
        this.name = events.getName();
        this.eventDate = events.getEventDate();
        this.totalCapacity = events.getTotalCapacity();
        this.remainingCapacity = events.getRemainingCapacity();
    }

    public EventsEntity(UUID id, String name, LocalDate eventDate, int totalCapacity, int remainingCapacity) {
        this.id = id;
        this.name = name;
        this.eventDate = eventDate;
        this.totalCapacity = totalCapacity;
        this.remainingCapacity = remainingCapacity;
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

    public Events toDomain() {
        return new Events(this);
    }
}
