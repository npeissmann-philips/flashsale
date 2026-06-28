package com.self.study.flashsale.flashsale.adapters.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import com.self.study.flashsale.flashsale.adapters.presenters.OrdersRequest;
import com.self.study.flashsale.flashsale.adapters.presenters.OrdersResponse;
import com.self.study.flashsale.flashsale.adapters.presenters.PagedResponse;
import com.self.study.flashsale.flashsale.application.useCase.orders.DeleteOrder;
import com.self.study.flashsale.flashsale.application.useCase.orders.FindAllOrders;
import com.self.study.flashsale.flashsale.application.useCase.orders.FindAllOrdersPaged;
import com.self.study.flashsale.flashsale.application.useCase.orders.FindOrderById;
import com.self.study.flashsale.flashsale.application.useCase.orders.SaveOrder;
import com.self.study.flashsale.flashsale.domain.models.Events;
import com.self.study.flashsale.flashsale.domain.models.Orders;
import com.self.study.flashsale.flashsale.domain.models.PagedResult;
import com.self.study.flashsale.flashsale.drivers.db.entities.enums.OrderStatus;

@ExtendWith(MockitoExtension.class)
class OrdersControllerImplTest {

    @Mock
    private SaveOrder saveOrder;

    @Mock
    private FindOrderById findOrder;

    @Mock
    private FindAllOrders findAllOrders;

    @Mock
    private FindAllOrdersPaged findAllOrdersPaged;

    @Mock
    private DeleteOrder deleteOrder;

    @InjectMocks
    private OrdersControllerImpl ordersController;

    @Test
    void shouldSaveOrderSuccessfully() {
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        OrdersRequest request = new OrdersRequest(eventId, null, LocalDate.now(), OrderStatus.PENDING, userId);
        
        Events event = Events.builder().id(eventId).build();
        Orders savedOrder = Orders.builder()
                .id(UUID.randomUUID())
                .eventId(event)
                .userId(userId)
                .orderDate(LocalDate.now())
                .status(OrderStatus.COMPLETED)
                .build();

        when(saveOrder.execute(any(Orders.class))).thenReturn(savedOrder);

        OrdersResponse response = ordersController.save(request);

        assertNotNull(response);
        assertEquals(savedOrder.getId(), response.getId());
        assertEquals(OrderStatus.COMPLETED.name(), response.getStatus().name());
        verify(saveOrder).execute(any(Orders.class));
    }

    @Test
    void shouldRetryAndEventuallySaveOrderSuccessfully() {
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        OrdersRequest request = new OrdersRequest(eventId, null, LocalDate.now(), OrderStatus.PENDING, userId);
        
        Events event = Events.builder().id(eventId).build();
        Orders savedOrder = Orders.builder()
                .id(UUID.randomUUID())
                .eventId(event)
                .userId(userId)
                .orderDate(LocalDate.now())
                .status(OrderStatus.COMPLETED)
                .build();

        when(saveOrder.execute(any(Orders.class)))
                .thenThrow(new ObjectOptimisticLockingFailureException(Orders.class, "optimistic lock"))
                .thenReturn(savedOrder);

        OrdersResponse response = ordersController.save(request);

        assertNotNull(response);
        assertEquals(savedOrder.getId(), response.getId());
        verify(saveOrder, times(2)).execute(any(Orders.class));
    }

    @Test
    void shouldPropagateOptimisticLockingFailureWhenMaxRetriesExceeded() {
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        OrdersRequest request = new OrdersRequest(eventId, null, LocalDate.now(), OrderStatus.PENDING, userId);

        when(saveOrder.execute(any(Orders.class)))
                .thenThrow(new ObjectOptimisticLockingFailureException(Orders.class, "optimistic lock"));

        assertThrows(ObjectOptimisticLockingFailureException.class, () -> ordersController.save(request));
        verify(saveOrder, times(5)).execute(any(Orders.class));
    }

    @Test
    void shouldFindOrderByIdSuccessfully() throws NotFoundException {
        UUID orderId = UUID.randomUUID();
        Events event = Events.builder().id(UUID.randomUUID()).build();
        Orders order = Orders.builder()
                .id(orderId)
                .eventId(event)
                .userId(UUID.randomUUID())
                .orderDate(LocalDate.now())
                .status(OrderStatus.COMPLETED)
                .build();

        when(findOrder.execute(orderId)).thenReturn(order);

        OrdersResponse response = ordersController.findById(orderId);

        assertNotNull(response);
        assertEquals(orderId, response.getId());
        verify(findOrder).execute(orderId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenOrderDoesNotExist() {
        UUID orderId = UUID.randomUUID();
        when(findOrder.execute(orderId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> ordersController.findById(orderId));
        verify(findOrder).execute(orderId);
    }

    @Test
    void shouldFindAllOrders() {
        UUID eventId = UUID.randomUUID();
        Events event = Events.builder().id(eventId).build();
        Orders order = Orders.builder()
                .id(UUID.randomUUID())
                .eventId(event)
                .build();
        when(findAllOrders.execute()).thenReturn(List.of(order));

        List<OrdersResponse> result = ordersController.findAll();

        assertEquals(1, result.size());
        assertEquals(order.getId(), result.get(0).getId());
        verify(findAllOrders).execute();
    }

    @Test
    void shouldFindAllPaged() {
        UUID eventId = UUID.randomUUID();
        Events event = Events.builder().id(eventId).build();
        Orders order = Orders.builder()
                .id(UUID.randomUUID())
                .eventId(event)
                .build();
        PagedResult<Orders> pagedResult = new PagedResult<>(List.of(order), 1, 1, 0, 10);
        when(findAllOrdersPaged.execute(0, 10)).thenReturn(pagedResult);

        PagedResponse<OrdersResponse> result = ordersController.findAllPaged(0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        verify(findAllOrdersPaged).execute(0, 10);
    }

    @Test
    void shouldDeleteOrder() {
        UUID orderId = UUID.randomUUID();

        ordersController.delete(orderId);

        verify(deleteOrder).execute(orderId);
    }
}
