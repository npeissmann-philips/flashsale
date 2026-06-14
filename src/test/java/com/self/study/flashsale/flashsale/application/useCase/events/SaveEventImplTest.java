package com.self.study.flashsale.flashsale.application.useCase.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.self.study.flashsale.flashsale.adapters.gateway.EventsGateway;
import com.self.study.flashsale.flashsale.domain.models.Events;

@ExtendWith(MockitoExtension.class)
class SaveEventImplTest {

    @Mock
    private EventsGateway eventsGateway;

    @InjectMocks
    private SaveEventImpl saveEvent;

    @Test
    void shouldSaveEventSuccessfullyWhenCapacityIsPositive() {
        Events event = Events.builder()
                .id(UUID.randomUUID())
                .name("Rock Festival")
                .eventDate(LocalDate.now().plusDays(10))
                .totalCapacity(100)
                .remainingCapacity(100)
                .version(1L)
                .build();

        when(eventsGateway.save(event)).thenReturn(event);

        Events result = saveEvent.execute(event);

        assertEquals(event, result);
        verify(eventsGateway).save(event);
    }

    @Test
    void shouldThrowExceptionWhenCapacityIsZero() {
        Events event = Events.builder()
                .id(UUID.randomUUID())
                .name("Rock Festival")
                .totalCapacity(0)
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> saveEvent.execute(event));
        assertEquals("Capacity must be positive", exception.getMessage());
        verifyNoInteractions(eventsGateway);
    }

    @Test
    void shouldThrowExceptionWhenCapacityIsNegative() {
        Events event = Events.builder()
                .id(UUID.randomUUID())
                .name("Rock Festival")
                .totalCapacity(-5)
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> saveEvent.execute(event));
        assertEquals("Capacity must be positive", exception.getMessage());
        verifyNoInteractions(eventsGateway);
    }
}
