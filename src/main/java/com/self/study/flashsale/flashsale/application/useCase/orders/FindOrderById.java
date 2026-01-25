package com.self.study.flashsale.flashsale.application.useCase.orders;

import java.util.UUID;

import com.self.study.flashsale.flashsale.domain.models.Orders;

public interface FindOrderById {
    Orders execute(UUID id);

}
