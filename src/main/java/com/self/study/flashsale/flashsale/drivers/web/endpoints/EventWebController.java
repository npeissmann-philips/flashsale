package com.self.study.flashsale.flashsale.drivers.web.endpoints;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.self.study.flashsale.flashsale.adapters.controllers.EventsController;
import com.self.study.flashsale.flashsale.adapters.presenters.EventRequest;
import com.self.study.flashsale.flashsale.adapters.presenters.EventResponse;
import com.self.study.flashsale.flashsale.adapters.presenters.PagedResponse;

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
    @Caching(evict = {
        @CacheEvict(value = "events_all", allEntries = true),
        @CacheEvict(value = "events_paged", allEntries = true)
    })
    public EventResponse save(@RequestBody EventRequest eventRequest) {
        return eventController.save(eventRequest);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find an event by id")
    @Cacheable(value = "events", key = "#id")
    public EventResponse findById(@PathVariable UUID id) throws NotFoundException {
        return eventController.findById(id);
    }

    @GetMapping
    @Operation(summary = "Find all events")
    @Cacheable(value = "events_all", key = "'list'")
    public List<EventResponse> findAll() {
        return eventController.findAll();
    }

    @GetMapping("/paged")
    @Operation(summary = "Find all events paged")
    @Cacheable(value = "events_paged", key = "#page + '-' + #size")
    public PagedResponse<EventResponse> findAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return eventController.findAllPaged(page, size);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an event by id")
    @Caching(evict = {
        @CacheEvict(value = "events", key = "#id"),
        @CacheEvict(value = "events_all", allEntries = true),
        @CacheEvict(value = "events_paged", allEntries = true)
    })
    public void delete(@PathVariable UUID id) {
        eventController.delete(id);
    }
}
