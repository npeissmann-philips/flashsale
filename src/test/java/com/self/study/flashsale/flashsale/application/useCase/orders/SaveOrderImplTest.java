package com.self.study.flashsale.flashsale.application.useCase.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.self.study.flashsale.flashsale.adapters.gateway.EventsGateway;
import com.self.study.flashsale.flashsale.adapters.gateway.OrdersGateway;
import com.self.study.flashsale.flashsale.domain.models.Events;
import com.self.study.flashsale.flashsale.domain.models.Orders;
import com.self.study.flashsale.flashsale.drivers.db.entities.enums.OrderStatus;

@ExtendWith(MockitoExtension.class)
class SaveOrderImplTest {

    @Mock
    private OrdersGateway ordersGateway;

    @Mock
    private EventsGateway eventsGateway;

    @InjectMocks
    private SaveOrderImpl saveOrder;

    @Test
    void shouldSaveOrderSuccessfullyWhenEventHasRemainingCapacity() {
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Events event = Events.builder()
                .id(eventId)
                .name("Flash Sale Event")
                .totalCapacity(10)
                .remainingCapacity(5)
                .build();

        Orders orderInput = Orders.builder()
                .eventId(event)
                .userId(userId)
                .orderDate(LocalDate.now())
                .status(OrderStatus.PENDING)
                .build();

        Events updatedEvent = Events.builder()
                .id(eventId)
                .name("Flash Sale Event")
                .totalCapacity(10)
                .remainingCapacity(4) // Decremented
                .build();

        Orders savedOrder = Orders.builder()
                .id(UUID.randomUUID())
                .eventId(updatedEvent)
                .userId(userId)
                .orderDate(orderInput.getOrderDate())
                .status(OrderStatus.COMPLETED)
                .build();

        when(eventsGateway.findByIdForUpdate(eventId)).thenReturn(event);
        when(eventsGateway.save(any(Events.class))).thenReturn(updatedEvent);
        when(ordersGateway.save(orderInput)).thenReturn(savedOrder);

        Orders result = saveOrder.execute(orderInput);

        assertEquals(savedOrder, result);
        assertEquals(4, event.getRemainingCapacity()); // Verify decrement happened on the event object
        verify(eventsGateway).findByIdForUpdate(eventId);
        verify(eventsGateway).save(event);
        verify(ordersGateway).save(orderInput);
    }

    @Test
    void shouldThrowExceptionAndNotSaveOrderWhenEventIsFull() {
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Events event = Events.builder()
                .id(eventId)
                .name("Flash Sale Event")
                .totalCapacity(10)
                .remainingCapacity(0) // Full event
                .build();

        Orders orderInput = Orders.builder()
                .eventId(event)
                .userId(userId)
                .orderDate(LocalDate.now())
                .status(OrderStatus.PENDING)
                .build();

        when(eventsGateway.findByIdForUpdate(eventId)).thenReturn(event);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> saveOrder.execute(orderInput));
        assertEquals("Event is full", exception.getMessage());

        verify(eventsGateway).findByIdForUpdate(eventId);
        verify(eventsGateway, never()).save(any(Events.class));
        verify(ordersGateway, never()).save(any(Orders.class));
    }
}
