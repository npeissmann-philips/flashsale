package com.self.study.flashsale.flashsale.drivers.messaging.consumer;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.self.study.flashsale.flashsale.adapters.controllers.OrdersController;
import com.self.study.flashsale.flashsale.adapters.presenters.OrdersRequest;
import com.self.study.flashsale.flashsale.drivers.db.entities.enums.OrderStatus;

@ExtendWith(MockitoExtension.class)
class OrdersConsumerTest {

    @Mock
    private OrdersController ordersController;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache ordersAllCache;

    @Mock
    private Cache ordersPagedCache;

    @InjectMocks
    private OrdersConsumer ordersConsumer;

    @Test
    void shouldConsumeOrderRequestSuccessfullyAndClearCaches() {
        OrdersRequest request = new OrdersRequest(UUID.randomUUID(), UUID.randomUUID(), LocalDate.now(), OrderStatus.PENDING, UUID.randomUUID());

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
