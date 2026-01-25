package com.self.study.flashsale.flashsale.drivers.db.entities;

import java.time.LocalDate;
import java.util.UUID;

import com.self.study.flashsale.flashsale.domain.models.Orders;
import com.self.study.flashsale.flashsale.drivers.db.entities.enums.OrderStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class OrdersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @jakarta.persistence.ManyToOne
    @jakarta.persistence.JoinColumn(name = "event_id")
    private EventsEntity event;
    private UUID userId;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private LocalDate orderDate;

    public OrdersEntity() {
    }

    public OrdersEntity(UUID id, EventsEntity event, UUID userId, OrderStatus status, LocalDate orderDate) {
        this.id = id;
        this.event = event;
        this.userId = userId;
        this.status = status;
        this.orderDate = orderDate;
    }

    public OrdersEntity(Orders orders) {
        this.id = orders.getId();
        this.event = orders.getEventId().toEntity();
        this.userId = orders.getUserId();
        this.status = orders.getStatus();
        this.orderDate = orders.getOrderDate();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public EventsEntity getEvent() {
        return event;
    }

    public void setEvent(EventsEntity event) {
        this.event = event;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public Orders toDomain() {
        return new Orders(this);
    }

}