package com.self.study.flashsale.flashsale.application.useCase.orders;

import com.self.study.flashsale.flashsale.adapters.gateway.OrdersGateway;
import com.self.study.flashsale.flashsale.domain.models.Orders;

public class SaveOrderImpl implements SaveOrder {

    private final OrdersGateway ordersGateway;

    public SaveOrderImpl(OrdersGateway ordersGateway) {
        this.ordersGateway = ordersGateway;
    }

    @Override
    public Orders execute(Orders order) {
        return ordersGateway.save(order);
    }
}
