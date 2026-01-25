package com.self.study.flashsale.flashsale.drivers.web.endpoints;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.self.study.flashsale.flashsale.adapters.controllers.EventsController;
import com.self.study.flashsale.flashsale.adapters.presenters.EventRequest;
import com.self.study.flashsale.flashsale.adapters.presenters.EventResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/events")
@Tag(name = "Events")
public class EventWebController {

    EventsController eventController;

    public EventWebController(EventsController eventController) {
        this.eventController = eventController;
    }

    @PostMapping
    @Operation(summary = "Save an event")
    public EventResponse save(@RequestBody EventRequest eventRequest) {
        return eventController.save(eventRequest);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find an event by id")
    public EventResponse findById(@PathVariable UUID id) {
        return eventController.findById(id);
    }

    @GetMapping
    @Operation(summary = "Find all events")
    public List<EventResponse> findAll() {
        return eventController.findAll();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an event by id")
    public void delete(@PathVariable UUID id) {
        eventController.delete(id);
    }
}
