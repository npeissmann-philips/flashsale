package com.self.study.flashsale.flashsale.application.useCase.orders;

import com.self.study.flashsale.flashsale.adapters.gateway.EventsGateway;
import com.self.study.flashsale.flashsale.adapters.gateway.OrdersGateway;
import com.self.study.flashsale.flashsale.domain.models.Events;
import com.self.study.flashsale.flashsale.domain.models.Orders;

public class SaveOrderImpl implements SaveOrder {

    private final OrdersGateway ordersGateway;
    private final EventsGateway eventsGateway;

    public SaveOrderImpl(OrdersGateway ordersGateway, EventsGateway eventsGateway) {
        this.ordersGateway = ordersGateway;
        this.eventsGateway = eventsGateway;
    }

    @Override
    public Orders execute(Orders order) {
        Events events = eventsGateway.findById(order.getEventId().getId());
        if (events.getRemainingCapacity() < 0) {
            throw new RuntimeException("Event is full");
        }
        events.setRemainingCapacity(events.getRemainingCapacity() - 1);
        eventsGateway.save(events);
        order.setEventId(events);
        return ordersGateway.save(order);
    }
}
