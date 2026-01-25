package com.self.study.flashsale.flashsale.domain.models;

import java.time.LocalDate;
import java.util.UUID;

import com.self.study.flashsale.flashsale.drivers.db.entities.OrdersEntity;
import com.self.study.flashsale.flashsale.drivers.db.entities.enums.OrderStatus;

public class Orders {
    private UUID id;
    private Events eventId;
    private UUID userId;
    private LocalDate orderDate;
    private OrderStatus status;

    public Orders() {
    }

    public Orders(OrdersEntity ordersEntity) {
        this.id = ordersEntity.getId();
        this.eventId = ordersEntity.getEvent().toDomain();
        this.userId = ordersEntity.getUserId();
        this.orderDate = ordersEntity.getOrderDate();
        this.status = ordersEntity.getStatus();
    }

    public Orders(UUID id, Events eventId, UUID userId, LocalDate orderDate, OrderStatus status) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Events getEventId() {
        return eventId;
    }

    public void setEventId(Events eventId) {
        this.eventId = eventId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public OrdersEntity toEntity() {
        return new OrdersEntity(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private Events eventId;
        private UUID userId;
        private LocalDate orderDate;
        private OrderStatus status;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder eventId(Events eventId) {
            this.eventId = eventId;
            return this;
        }

        public Builder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public Builder orderDate(LocalDate orderDate) {
            this.orderDate = orderDate;
            return this;
        }

        public Builder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public Orders build() {
            return new Orders(id, eventId, userId, orderDate, status);
        }
    }
}
