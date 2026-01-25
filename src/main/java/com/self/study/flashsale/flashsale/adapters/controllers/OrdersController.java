package com.self.study.flashsale.flashsale.adapters.controllers;

import java.util.List;
import java.util.UUID;

import com.self.study.flashsale.flashsale.adapters.presenters.OrdersRequest;
import com.self.study.flashsale.flashsale.adapters.presenters.OrdersResponse;

public interface OrdersController {
    OrdersResponse save(OrdersRequest ordersRequest);

    OrdersResponse findById(UUID id);

    List<OrdersResponse> findAll();

    void delete(UUID id);
}