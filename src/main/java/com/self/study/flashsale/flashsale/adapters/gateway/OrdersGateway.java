package com.self.study.flashsale.flashsale.adapters.gateway;

import java.util.List;
import java.util.UUID;

import com.self.study.flashsale.flashsale.domain.models.Orders;

public interface OrdersGateway {
    Orders save(Orders order);

    Orders findById(UUID id);

    void delete(UUID id);

    List<Orders> findAll();
}