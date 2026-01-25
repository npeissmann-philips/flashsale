package com.self.study.flashsale.flashsale.application.useCase.events;

import java.util.List;

import org.springframework.stereotype.Service;

import com.self.study.flashsale.flashsale.adapters.gateway.EventsGateway;
import com.self.study.flashsale.flashsale.domain.models.Events;

@Service
public class FindAllEventsImpl implements FindAllEvents {

    private final EventsGateway eventsGateway;

    public FindAllEventsImpl(EventsGateway eventsGateway) {
        this.eventsGateway = eventsGateway;
    }

    @Override
    public List<Events> execute() {
        return eventsGateway.findAll();
    }
}
