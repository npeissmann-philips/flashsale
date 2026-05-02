package com.self.study.flashsale.flashsale.application.useCase.orders;

import com.self.study.flashsale.flashsale.adapters.gateway.EventsGateway;
import com.self.study.flashsale.flashsale.adapters.gateway.OrdersGateway;
import com.self.study.flashsale.flashsale.domain.models.Events;
import com.self.study.flashsale.flashsale.domain.models.Orders;

import jakarta.transaction.Transactional;

public class SaveOrderImpl implements SaveOrder {

    private final OrdersGateway ordersGateway;
    private final EventsGateway eventsGateway;

    public SaveOrderImpl(OrdersGateway ordersGateway, EventsGateway eventsGateway) {
        this.ordersGateway = ordersGateway;
        this.eventsGateway = eventsGateway;
    }

    @Override
    @Transactional
    public Orders execute(Orders order) {

        Events events = eventsGateway.findByIdForUpdate(order.getEventId().getId());
        events.setRemainingCapacity(events.getRemainingCapacity() - 1);
        if (events.getRemainingCapacity() < 0) {
            throw new RuntimeException("Event is full");
        }
        eventsGateway.save(events);
        order.setEventId(events);
        return ordersGateway.save(order);
    }
}
