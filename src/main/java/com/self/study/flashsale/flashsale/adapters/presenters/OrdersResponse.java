package com.self.study.flashsale.flashsale.adapters.presenters;

import java.time.LocalDate;
import java.util.UUID;
import com.self.study.flashsale.flashsale.domain.models.Orders;
import com.self.study.flashsale.flashsale.drivers.db.entities.enums.OrderStatus;

public class OrdersResponse {

    private EventResponse eventId;
    private UUID id;
    private LocalDate orderDate;
    private OrderStatus status;
    private UUID userId;

    public OrdersResponse(Orders orders) {
        this.eventId = new EventResponse(orders.getEventId());
        this.id = orders.getId();
        this.orderDate = orders.getOrderDate();
        this.status = orders.getStatus();
        this.userId = orders.getUserId();
    }

    public EventResponse getEventId() {
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
}
