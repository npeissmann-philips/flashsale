package com.self.study.flashsale.flashsale.adapters.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.self.study.flashsale.flashsale.adapters.presenters.EventRequest;
import com.self.study.flashsale.flashsale.adapters.presenters.EventResponse;
import com.self.study.flashsale.flashsale.application.useCase.events.DeleteEvent;
import com.self.study.flashsale.flashsale.application.useCase.events.FindAllEventsImpl;
import com.self.study.flashsale.flashsale.application.useCase.events.FindEventByIdImpl;
import com.self.study.flashsale.flashsale.application.useCase.events.SaveEvent;

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
    public EventResponse findById(UUID id) {
        return new EventResponse(findEvent.execute(id));
    }

    @Override
    public List<EventResponse> findAll() {
        return findAllEvents.execute().stream().map(EventResponse::new).toList();
    }

    @Override
    public void delete(UUID id) {
        deleteEvent.execute(id);
    }
}
