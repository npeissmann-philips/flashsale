package com.self.study.flashsale.flashsale.drivers.messaging.consumer;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.lenient;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.self.study.flashsale.flashsale.adapters.controllers.EventsController;
import com.self.study.flashsale.flashsale.adapters.controllers.OrdersController;
import com.self.study.flashsale.flashsale.adapters.presenters.EventResponse;
import com.self.study.flashsale.flashsale.adapters.presenters.OrdersRequest;
import com.self.study.flashsale.flashsale.adapters.presenters.OrdersResponse;
import com.self.study.flashsale.flashsale.drivers.db.entities.enums.OrderStatus;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@ExtendWith(MockitoExtension.class)
class OrdersConsumerTest {

    @Mock
    private OrdersController ordersController;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private EventsController eventsController;

    @Mock
    private MeterRegistry meterRegistry;

    @Mock
    private Counter counter;

    @Mock
    private Cache ordersAllCache;

    @Mock
    private Cache ordersPagedCache;

    @InjectMocks
    private OrdersConsumer ordersConsumer;

    @BeforeEach
    void setUp() {
        lenient().when(meterRegistry.counter(anyString(), any(String[].class))).thenReturn(counter);
    }

    @Test
    void shouldConsumeOrderRequestSuccessfullyAndClearCaches() throws Exception {
        UUID eventId = UUID.randomUUID();
        OrdersRequest request = new OrdersRequest(eventId, UUID.randomUUID(), LocalDate.now(), OrderStatus.PENDING, UUID.randomUUID());

        EventResponse eventResponse = new EventResponse();
        eventResponse.setName("Test Event");
        when(eventsController.findById(eventId)).thenReturn(eventResponse);

        OrdersResponse response = new OrdersResponse();
        response.setEventId(eventResponse);
        when(ordersController.save(request)).thenReturn(response);

        when(cacheManager.getCache("orders_all")).thenReturn(ordersAllCache);
        when(cacheManager.getCache("orders_paged")).thenReturn(ordersPagedCache);

        ordersConsumer.consumeOrderRequest(request);

        verify(ordersController).save(request);
        verify(ordersAllCache).clear();
        verify(ordersPagedCache).clear();
    }

    @Test
    void shouldHandleExceptionDuringConsume() {
        OrdersRequest request = new OrdersRequest(UUID.randomUUID(), UUID.randomUUID(), LocalDate.now(), OrderStatus.PENDING, UUID.randomUUID());

        doThrow(new RuntimeException("Database error")).when(ordersController).save(request);

        ordersConsumer.consumeOrderRequest(request);

        verify(ordersController).save(request);
    }
}

