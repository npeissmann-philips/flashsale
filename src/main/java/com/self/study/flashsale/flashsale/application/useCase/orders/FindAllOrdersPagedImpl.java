package com.self.study.flashsale.flashsale.application.useCase.orders;

import com.self.study.flashsale.flashsale.adapters.gateway.OrdersGateway;
import com.self.study.flashsale.flashsale.domain.models.Orders;
import com.self.study.flashsale.flashsale.domain.models.PagedResult;

public class FindAllOrdersPagedImpl implements FindAllOrdersPaged {

    private final OrdersGateway ordersGateway;

    public FindAllOrdersPagedImpl(OrdersGateway ordersGateway) {
        this.ordersGateway = ordersGateway;
    }

    @Override
    public PagedResult<Orders> execute(int page, int size) {
        return ordersGateway.findAllPaged(page, size);
    }
}
