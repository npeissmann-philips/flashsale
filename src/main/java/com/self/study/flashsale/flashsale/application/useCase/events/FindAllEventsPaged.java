package com.self.study.flashsale.flashsale.application.useCase.events;

import com.self.study.flashsale.flashsale.domain.models.Events;
import com.self.study.flashsale.flashsale.domain.models.PagedResult;

public interface FindAllEventsPaged {
    PagedResult<Events> execute(int page, int size);
}
