package com.self.study.flashsale.flashsale.adapters.controllers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Component;

import com.self.study.flashsale.flashsale.adapters.presenters.EventRequest;
import com.self.study.flashsale.flashsale.adapters.presenters.EventResponse;
import com.self.study.flashsale.flashsale.application.useCase.events.DeleteEvent;
import com.self.study.flashsale.flashsale.application.useCase.events.FindAllEventsImpl;
import com.self.study.flashsale.flashsale.application.useCase.events.FindEventByIdImpl;
import com.self.study.flashsale.flashsale.application.useCase.events.SaveEvent;
import com.self.study.flashsale.flashsale.domain.models.Events;

@Component
public class EventsControllerImpl implements EventsController {

    @Autowired
    private SaveEvent saveEvent;
    @Autowired
    private FindEventByIdImpl findEvent;
    @Autowired
    private FindAllEventsImpl findAllEvents;
    @Autowired
    private DeleteEvent deleteEvent;

    public EventsControllerImpl(SaveEvent saveEvent, FindEventByIdImpl findEvent, FindAllEventsImpl findAllEvents,
            DeleteEvent deleteEvent) {
        this.saveEvent = saveEvent;
        this.findEvent = findEvent;
        this.findAllEvents = findAllEvents;
        this.deleteEvent = deleteEvent;
    }

    @Override
    public EventResponse save(EventRequest eventRequest) {
        return new EventResponse(saveEvent.execute(eventRequest.toDomain()));
    }

    @Override
    public EventResponse findById(UUID id) throws NotFoundException {
        Events event = findEvent.execute(id);
        if (event == null) {
            throw new NotFoundException();
        }
        return new EventResponse(event);
    }

    @Override
    public List<EventResponse> findAll() {
        return findAllEvents.execute().stream().map(EventResponse::new).collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        deleteEvent.execute(id);
    }
}
