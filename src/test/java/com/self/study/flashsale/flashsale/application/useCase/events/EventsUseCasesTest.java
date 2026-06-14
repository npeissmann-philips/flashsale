package com.self.study.flashsale.flashsale.application.useCase.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.self.study.flashsale.flashsale.adapters.gateway.EventsGateway;
import com.self.study.flashsale.flashsale.domain.models.Events;
import com.self.study.flashsale.flashsale.domain.models.PagedResult;

@ExtendWith(MockitoExtension.class)
class EventsUseCasesTest {

    @Mock
    private EventsGateway eventsGateway;

    @InjectMocks
    private DeleteEventImpl deleteEvent;

    @InjectMocks
    private FindAllEventsImpl findAllEvents;

    @InjectMocks
    private FindAllEventsPagedImpl findAllEventsPaged;

    @InjectMocks
    private FindEventByIdImpl findEventById;

    @Test
    void shouldDeleteEvent() {
        UUID eventId = UUID.randomUUID();
        deleteEvent.execute(eventId);
        verify(eventsGateway).delete(eventId);
    }

    @Test
    void shouldFindAllEvents() {
        Events event = Events.builder().id(UUID.randomUUID()).name("Test Event").build();
        when(eventsGateway.findAll()).thenReturn(List.of(event));

        List<Events> result = findAllEvents.execute();

        assertEquals(1, result.size());
        assertEquals(event, result.get(0));
        verify(eventsGateway).findAll();
    }

    @Test
    void shouldFindAllEventsPaged() {
        Events event = Events.builder().id(UUID.randomUUID()).name("Test Event").build();
        PagedResult<Events> pagedResult = new PagedResult<>(List.of(event), 1, 1, 0, 10);
        when(eventsGateway.findAllPaged(0, 10)).thenReturn(pagedResult);

        PagedResult<Events> result = findAllEventsPaged.execute(0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals(event, result.getContent().get(0));
        verify(eventsGateway).findAllPaged(0, 10);
    }

    @Test
    void shouldFindEventById() {
        UUID eventId = UUID.randomUUID();
        Events event = Events.builder().id(eventId).name("Test Event").build();
        when(eventsGateway.findById(eventId)).thenReturn(event);

        Events result = findEventById.execute(eventId);

        assertEquals(event, result);
        verify(eventsGateway).findById(eventId);
    }

    @Test
    void shouldReturnNullWhenEventByIdNotFound() {
        UUID eventId = UUID.randomUUID();
        when(eventsGateway.findById(eventId)).thenReturn(null);

        Events result = findEventById.execute(eventId);

        assertNull(result);
        verify(eventsGateway).findById(eventId);
    }
}
