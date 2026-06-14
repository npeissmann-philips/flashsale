package com.self.study.flashsale.flashsale.application.useCase.events;

import org.springframework.stereotype.Service;

import com.self.study.flashsale.flashsale.adapters.gateway.EventsGateway;
import com.self.study.flashsale.flashsale.domain.models.Events;
import com.self.study.flashsale.flashsale.domain.models.PagedResult;

@Service
public class FindAllEventsPagedImpl implements FindAllEventsPaged {

    private final EventsGateway eventsGateway;

    public FindAllEventsPagedImpl(EventsGateway eventsGateway) {
        this.eventsGateway = eventsGateway;
    }

    @Override
    public PagedResult<Events> execute(int page, int size) {
        return eventsGateway.findAllPaged(page, size);
    }
}
