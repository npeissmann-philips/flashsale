package com.self.study.flashsale.flashsale.adapters.gateway;

import java.util.List;
import java.util.UUID;

import com.self.study.flashsale.flashsale.domain.models.Orders;
import com.self.study.flashsale.flashsale.domain.models.PagedResult;

public interface OrdersGateway {
    Orders save(Orders order);

    Orders findById(UUID id);

    void delete(UUID id);

    List<Orders> findAll();

    PagedResult<Orders> findAllPaged(int page, int size);
}