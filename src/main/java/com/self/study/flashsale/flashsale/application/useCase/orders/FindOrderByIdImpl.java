package com.self.study.flashsale.flashsale.application.useCase.orders;

import java.util.UUID;

import com.self.study.flashsale.flashsale.adapters.gateway.OrdersGateway;
import com.self.study.flashsale.flashsale.domain.models.Orders;

public class FindOrderByIdImpl implements FindOrderById {

    private final OrdersGateway ordersGateway;

    public FindOrderByIdImpl(OrdersGateway ordersGateway) {
        this.ordersGateway = ordersGateway;
    }

    @Override
    public Orders execute(UUID id) {
        return ordersGateway.findById(id);
    }
}
