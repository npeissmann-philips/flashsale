package com.self.study.flashsale.flashsale.adapters.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.self.study.flashsale.flashsale.adapters.presenters.OrdersRequest;
import com.self.study.flashsale.flashsale.adapters.presenters.OrdersResponse;

public interface OrdersController {
    OrdersResponse save(OrdersRequest ordersRequest);

    OrdersResponse findById(UUID id) throws NotFoundException;

    List<OrdersResponse> findAll();

    void delete(UUID id);
}