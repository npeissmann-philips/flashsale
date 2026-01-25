package com.self.study.flashsale.flashsale.adapters.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.self.study.flashsale.flashsale.adapters.presenters.OrdersRequest;
import com.self.study.flashsale.flashsale.adapters.presenters.OrdersResponse;
import com.self.study.flashsale.flashsale.application.useCase.orders.DeleteOrder;
import com.self.study.flashsale.flashsale.application.useCase.orders.FindAllOrders;
import com.self.study.flashsale.flashsale.application.useCase.orders.FindOrderById;
import com.self.study.flashsale.flashsale.application.useCase.orders.SaveOrder;

@Component
public class OrdersControllerImpl implements OrdersController {

    @Autowired
    private SaveOrder saveOrder;
    @Autowired
    private FindOrderById findOrder;
    @Autowired
    private FindAllOrders findAllOrders;
    @Autowired
    private DeleteOrder deleteOrder;

    public OrdersControllerImpl(SaveOrder saveOrder, FindOrderById findOrder, FindAllOrders findAllOrders,
            DeleteOrder deleteOrder) {
        this.saveOrder = saveOrder;
        this.findOrder = findOrder;
        this.findAllOrders = findAllOrders;
        this.deleteOrder = deleteOrder;
    }

    @Override
    public OrdersResponse save(OrdersRequest orderRequest) {
        return new OrdersResponse(saveOrder.execute(orderRequest.toDomain()));
    }

    @Override
    public OrdersResponse findById(UUID id) {
        return new OrdersResponse(findOrder.execute(id));
    }

    @Override
    public List<OrdersResponse> findAll() {
        return findAllOrders.execute().stream().map(OrdersResponse::new).toList();
    }

    @Override
    public void delete(UUID id) {
        deleteOrder.execute(id);
    }
}
