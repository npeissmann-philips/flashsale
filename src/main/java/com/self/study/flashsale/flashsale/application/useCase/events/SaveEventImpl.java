package com.self.study.flashsale.flashsale.application.useCase.events;

import org.springframework.stereotype.Service;

import com.self.study.flashsale.flashsale.domain.models.Events;
import com.self.study.flashsale.flashsale.adapters.gateway.EventsGateway;

@Service
public class SaveEventImpl implements SaveEvent {

    private final EventsGateway eventsGateway;

    public SaveEventImpl(EventsGateway eventsGateway) {
        this.eventsGateway = eventsGateway;
    }

    @Override
    public Events execute(Events event) {
        if (event.getTotalCapacity() <= 0)
            throw new IllegalArgumentException("Capacity must be positive");

        return eventsGateway.save(event);
    }
}
