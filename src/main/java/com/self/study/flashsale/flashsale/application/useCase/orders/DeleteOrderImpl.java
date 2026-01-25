package com.self.study.flashsale.flashsale.application.useCase.orders;

import java.util.UUID;

import com.self.study.flashsale.flashsale.adapters.gateway.OrdersGateway;

public class DeleteOrderImpl implements DeleteOrder {

    private final OrdersGateway ordersGateway;

    public DeleteOrderImpl(OrdersGateway ordersGateway) {
        this.ordersGateway = ordersGateway;
    }

    @Override
    public void execute(UUID id) {
        ordersGateway.delete(id);
    }
}
