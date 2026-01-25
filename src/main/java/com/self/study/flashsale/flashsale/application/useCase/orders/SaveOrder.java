package com.self.study.flashsale.flashsale.application.useCase.orders;

import com.self.study.flashsale.flashsale.domain.models.Orders;

public interface SaveOrder {
    Orders execute(Orders order);
}