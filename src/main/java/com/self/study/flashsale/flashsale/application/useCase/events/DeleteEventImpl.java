package com.self.study.flashsale.flashsale.application.useCase.events;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.self.study.flashsale.flashsale.adapters.gateway.EventsGateway;

@Service
public class DeleteEventImpl implements DeleteEvent {

    private final EventsGateway eventsGateway;

    public DeleteEventImpl(EventsGateway eventsGateway) {
        this.eventsGateway = eventsGateway;
    }

    @Override
    public void execute(UUID id) {
        eventsGateway.delete(id);
    }
}
