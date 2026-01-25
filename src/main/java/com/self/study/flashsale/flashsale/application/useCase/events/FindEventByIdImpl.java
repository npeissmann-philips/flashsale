package com.self.study.flashsale.flashsale.application.useCase.events;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.self.study.flashsale.flashsale.adapters.gateway.EventsGateway;
import com.self.study.flashsale.flashsale.domain.models.Events;

@Service
public class FindEventByIdImpl implements FindEventById {

    private final EventsGateway eventsGateway;

    public FindEventByIdImpl(EventsGateway eventsGateway) {
        this.eventsGateway = eventsGateway;
    }

    @Override
    public Events execute(UUID id) {
        return eventsGateway.findById(id);
    }
}
