package com.self.study.flashsale.flashsale.application.useCase.orders;

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

import com.self.study.flashsale.flashsale.adapters.gateway.OrdersGateway;
import com.self.study.flashsale.flashsale.domain.models.Orders;
import com.self.study.flashsale.flashsale.domain.models.PagedResult;

@ExtendWith(MockitoExtension.class)
class OrdersUseCasesTest {

    @Mock
    private OrdersGateway ordersGateway;

    @InjectMocks
    private DeleteOrderImpl deleteOrder;

    @InjectMocks
    private FindAllOrdersImpl findAllOrders;

    @InjectMocks
    private FindAllOrdersPagedImpl findAllOrdersPaged;

    @InjectMocks
    private FindOrderByIdImpl findOrderById;

    @Test
    void shouldDeleteOrder() {
        UUID orderId = UUID.randomUUID();
        deleteOrder.execute(orderId);
        verify(ordersGateway).delete(orderId);
    }

    @Test
    void shouldFindAllOrders() {
        Orders order = Orders.builder().id(UUID.randomUUID()).build();
        when(ordersGateway.findAll()).thenReturn(List.of(order));

        List<Orders> result = findAllOrders.execute();

        assertEquals(1, result.size());
        assertEquals(order, result.get(0));
        verify(ordersGateway).findAll();
    }

    @Test
    void shouldFindAllOrdersPaged() {
        Orders order = Orders.builder().id(UUID.randomUUID()).build();
        PagedResult<Orders> pagedResult = new PagedResult<>(List.of(order), 1, 1, 0, 10);
        when(ordersGateway.findAllPaged(0, 10)).thenReturn(pagedResult);

        PagedResult<Orders> result = findAllOrdersPaged.execute(0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals(order, result.getContent().get(0));
        verify(ordersGateway).findAllPaged(0, 10);
    }

    @Test
    void shouldFindOrderById() {
        UUID orderId = UUID.randomUUID();
        Orders order = Orders.builder().id(orderId).build();
        when(ordersGateway.findById(orderId)).thenReturn(order);

        Orders result = findOrderById.execute(orderId);

        assertEquals(order, result);
        verify(ordersGateway).findById(orderId);
    }

    @Test
    void shouldReturnNullWhenOrderByIdNotFound() {
        UUID orderId = UUID.randomUUID();
        when(ordersGateway.findById(orderId)).thenReturn(null);

        Orders result = findOrderById.execute(orderId);

        assertNull(result);
        verify(ordersGateway).findById(orderId);
    }
}
