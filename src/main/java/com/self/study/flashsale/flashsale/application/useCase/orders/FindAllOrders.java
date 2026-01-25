package com.self.study.flashsale.flashsale.application.useCase.orders;

import java.util.List;

import com.self.study.flashsale.flashsale.domain.models.Orders;

public interface FindAllOrders {
    List<Orders> execute();
}
