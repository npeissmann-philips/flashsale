package com.self.study.flashsale.flashsale.adapters.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.self.study.flashsale.flashsale.adapters.presenters.EventRequest;
import com.self.study.flashsale.flashsale.adapters.presenters.EventResponse;
import com.self.study.flashsale.flashsale.adapters.presenters.PagedResponse;
import com.self.study.flashsale.flashsale.application.useCase.events.DeleteEvent;
import com.self.study.flashsale.flashsale.application.useCase.events.FindAllEventsImpl;
import com.self.study.flashsale.flashsale.application.useCase.events.FindAllEventsPaged;
import com.self.study.flashsale.flashsale.application.useCase.events.FindEventByIdImpl;
import com.self.study.flashsale.flashsale.application.useCase.events.SaveEvent;
import com.self.study.flashsale.flashsale.domain.models.Events;
import com.self.study.flashsale.flashsale.domain.models.PagedResult;

@ExtendWith(MockitoExtension.class)
class EventsControllerImplTest {

    @Mock
    private SaveEvent saveEvent;

    @Mock
    private FindEventByIdImpl findEvent;

    @Mock
    private FindAllEventsImpl findAllEvents;

    @Mock
    private FindAllEventsPaged findAllEventsPaged;

    @Mock
    private DeleteEvent deleteEvent;

    @InjectMocks
    private EventsControllerImpl eventsController;

    @Test
    void shouldSaveEventSuccessfully() {
        EventRequest request = new EventRequest(null, "Music Concert", 100, 100, LocalDate.now().plusDays(5));
        Events savedEvent = Events.builder()
                .id(UUID.randomUUID())
                .name("Music Concert")
                .eventDate(LocalDate.now().plusDays(5))
                .totalCapacity(100)
                .remainingCapacity(100)
                .version(1L)
                .build();

        when(saveEvent.execute(any(Events.class))).thenReturn(savedEvent);

        EventResponse response = eventsController.save(request);

        assertNotNull(response);
        assertEquals(savedEvent.getId(), response.getId());
        assertEquals("Music Concert", response.getName());
        verify(saveEvent).execute(any(Events.class));
    }

    @Test
    void shouldFindEventByIdSuccessfully() throws NotFoundException {
        UUID eventId = UUID.randomUUID();
        Events event = Events.builder()
                .id(eventId)
                .name("Music Concert")
                .eventDate(LocalDate.now().plusDays(5))
                .totalCapacity(100)
                .remainingCapacity(100)
                .version(1L)
                .build();

        when(findEvent.execute(eventId)).thenReturn(event);

        EventResponse response = eventsController.findById(eventId);

        assertNotNull(response);
        assertEquals(eventId, response.getId());
        verify(findEvent).execute(eventId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenEventDoesNotExist() {
        UUID eventId = UUID.randomUUID();
        when(findEvent.execute(eventId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> eventsController.findById(eventId));
        verify(findEvent).execute(eventId);
    }

    @Test
    void shouldFindAllEvents() {
        Events event = Events.builder()
                .id(UUID.randomUUID())
                .name("Concert")
                .build();
        when(findAllEvents.execute()).thenReturn(List.of(event));

        List<EventResponse> result = eventsController.findAll();

        assertEquals(1, result.size());
        assertEquals(event.getId(), result.get(0).getId());
        verify(findAllEvents).execute();
    }

    @Test
    void shouldFindAllPaged() {
        Events event = Events.builder()
                .id(UUID.randomUUID())
                .name("Concert")
                .build();
        PagedResult<Events> pagedResult = new PagedResult<>(List.of(event), 1, 1, 0, 10);
        when(findAllEventsPaged.execute(0, 10)).thenReturn(pagedResult);

        PagedResponse<EventResponse> result = eventsController.findAllPaged(0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        verify(findAllEventsPaged).execute(0, 10);
    }

    @Test
    void shouldDeleteEvent() {
        UUID eventId = UUID.randomUUID();

        eventsController.delete(eventId);

        verify(deleteEvent).execute(eventId);
    }
}
