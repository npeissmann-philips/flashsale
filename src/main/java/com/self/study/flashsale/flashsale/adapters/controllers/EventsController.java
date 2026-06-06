package com.self.study.flashsale.flashsale.adapters.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.self.study.flashsale.flashsale.adapters.presenters.EventRequest;
import com.self.study.flashsale.flashsale.adapters.presenters.EventResponse;

public interface EventsController {
    EventResponse save(EventRequest eventRequest);

    EventResponse findById(UUID id) throws NotFoundException;

    List<EventResponse> findAll();

    void delete(UUID id);
}
