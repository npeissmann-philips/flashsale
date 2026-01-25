package com.self.study.flashsale.flashsale.application.useCase.orders;

import java.util.List;

import com.self.study.flashsale.flashsale.adapters.gateway.OrdersGateway;
import com.self.study.flashsale.flashsale.domain.models.Orders;

public class FindAllOrdersImpl implements FindAllOrders {

    private final OrdersGateway ordersGateway;

    public FindAllOrdersImpl(OrdersGateway ordersGateway) {
        this.ordersGateway = ordersGateway;
    }

    @Override
    public List<Orders> execute() {
        return ordersGateway.findAll();
    }
}
