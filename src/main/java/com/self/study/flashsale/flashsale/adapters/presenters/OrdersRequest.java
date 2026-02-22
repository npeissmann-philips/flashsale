package com.self.study.flashsale.flashsale.adapters.presenters;

import java.time.LocalDate;
import java.util.UUID;

import com.self.study.flashsale.flashsale.domain.models.Orders;
import com.self.study.flashsale.flashsale.domain.models.Events;
import com.self.study.flashsale.flashsale.drivers.db.entities.enums.OrderStatus;

public class OrdersRequest {
    private UUID eventId;
    private UUID id;
    private LocalDate orderDate;
    private OrderStatus status;
    private UUID userId;

    public OrdersRequest() {
    }

    public OrdersRequest(UUID eventId, UUID id, LocalDate orderDate, OrderStatus status, UUID userId) {
        this.eventId = eventId;
        this.id = id;
        this.orderDate = orderDate;
        this.status = status;
        this.userId = userId;
    }

    public UUID getEventId() {
        return eventId;
    }

    public UUID getId() {
        return id;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public UUID getUserId() {
        return userId;
    }

    public Orders toDomain() {
        return Orders.builder()
                .id(id)
                .eventId(Events.builder().id(eventId).build())
                .userId(userId)
                .orderDate(orderDate)
                .status(status)
                .build();
    }
}
