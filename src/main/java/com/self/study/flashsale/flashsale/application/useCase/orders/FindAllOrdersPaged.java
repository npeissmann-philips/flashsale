package com.self.study.flashsale.flashsale.application.useCase.orders;

import com.self.study.flashsale.flashsale.domain.models.Orders;
import com.self.study.flashsale.flashsale.domain.models.PagedResult;

public interface FindAllOrdersPaged {
    PagedResult<Orders> execute(int page, int size);
}
