package com.self.study.flashsale.flashsale.drivers.web.endpoints;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.MediaType;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.self.study.flashsale.flashsale.adapters.controllers.EventsController;
import com.self.study.flashsale.flashsale.adapters.controllers.OrdersController;
import com.self.study.flashsale.flashsale.adapters.presenters.EventRequest;
import com.self.study.flashsale.flashsale.adapters.presenters.EventResponse;
import com.self.study.flashsale.flashsale.adapters.presenters.OrdersRequest;
import com.self.study.flashsale.flashsale.adapters.presenters.OrdersResponse;
import com.self.study.flashsale.flashsale.adapters.presenters.PagedResponse;
import com.self.study.flashsale.flashsale.domain.models.Events;
import com.self.study.flashsale.flashsale.domain.models.Orders;
import com.self.study.flashsale.flashsale.drivers.db.entities.enums.OrderStatus;
import com.self.study.flashsale.flashsale.drivers.messaging.producer.OrdersProducer;

@WebMvcTest(controllers = {EventWebController.class, OrdersWebController.class})
public class WebControllersTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
            .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @MockitoBean
    private EventsController eventController;

    @MockitoBean
    private OrdersController ordersController;

    @MockitoBean
    private OrdersProducer ordersProducer;

    @Test
    public void shouldReturn404WhenEventNotFound() throws Exception {
        UUID eventId = UUID.randomUUID();
        when(eventController.findById(eventId)).thenThrow(new NotFoundException());

        mockMvc.perform(get("/events/" + eventId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenOrderNotFound() throws Exception {
        UUID orderId = UUID.randomUUID();
        when(ordersController.findById(orderId)).thenThrow(new NotFoundException());

        mockMvc.perform(get("/orders/" + orderId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldSaveEventSuccessfully() throws Exception {
        EventRequest request = new EventRequest(null, "Concert", 100, 100, LocalDate.now());
        Events eventDomain = Events.builder()
                .id(UUID.randomUUID())
                .name("Concert")
                .eventDate(LocalDate.now())
                .totalCapacity(100)
                .remainingCapacity(100)
                .version(1L)
                .build();
        EventResponse response = new EventResponse(eventDomain);

        when(eventController.save(any(EventRequest.class))).thenReturn(response);

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void shouldFindEventByIdSuccessfully() throws Exception {
        UUID eventId = UUID.randomUUID();
        Events eventDomain = Events.builder()
                .id(eventId)
                .name("Concert")
                .eventDate(LocalDate.now())
                .totalCapacity(100)
                .remainingCapacity(100)
                .version(1L)
                .build();
        EventResponse response = new EventResponse(eventDomain);

        when(eventController.findById(eventId)).thenReturn(response);

        mockMvc.perform(get("/events/" + eventId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void shouldFindAllEventsSuccessfully() throws Exception {
        Events eventDomain = Events.builder()
                .id(UUID.randomUUID())
                .name("Concert")
                .eventDate(LocalDate.now())
                .totalCapacity(100)
                .remainingCapacity(100)
                .version(1L)
                .build();
        EventResponse response = new EventResponse(eventDomain);
        List<EventResponse> responseList = List.of(response);

        when(eventController.findAll()).thenReturn(responseList);

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseList)));
    }

    @Test
    public void shouldFindAllEventsPagedSuccessfully() throws Exception {
        Events eventDomain = Events.builder()
                .id(UUID.randomUUID())
                .name("Concert")
                .eventDate(LocalDate.now())
                .totalCapacity(100)
                .remainingCapacity(100)
                .version(1L)
                .build();
        EventResponse response = new EventResponse(eventDomain);
        PagedResponse<EventResponse> pagedResponse = new PagedResponse<>(List.of(response), 1, 1, 0, 10);

        when(eventController.findAllPaged(0, 10)).thenReturn(pagedResponse);

        mockMvc.perform(get("/events/paged?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pagedResponse)));
    }

    @Test
    public void shouldDeleteEventSuccessfully() throws Exception {
        UUID eventId = UUID.randomUUID();
        doNothing().when(eventController).delete(eventId);

        mockMvc.perform(delete("/events/" + eventId))
                .andExpect(status().isOk());

        verify(eventController).delete(eventId);
    }

    @Test
    public void shouldSaveOrderSuccessfully() throws Exception {
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        OrdersRequest request = new OrdersRequest(eventId, null, LocalDate.now(), OrderStatus.PENDING, userId);

        doNothing().when(ordersProducer).sendOrderRequest(any(OrdersRequest.class));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Order request accepted for processing"));

        verify(ordersProducer).sendOrderRequest(any(OrdersRequest.class));
    }

    @Test
    public void shouldFindOrderByIdSuccessfully() throws Exception {
        UUID orderId = UUID.randomUUID();
        Orders orderDomain = Orders.builder()
                .id(orderId)
                .eventId(Events.builder().id(UUID.randomUUID()).build())
                .userId(UUID.randomUUID())
                .orderDate(LocalDate.now())
                .status(OrderStatus.COMPLETED)
                .build();
        OrdersResponse response = new OrdersResponse(orderDomain);

        when(ordersController.findById(orderId)).thenReturn(response);

        mockMvc.perform(get("/orders/" + orderId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void shouldFindAllOrdersSuccessfully() throws Exception {
        Orders orderDomain = Orders.builder()
                .id(UUID.randomUUID())
                .eventId(Events.builder().id(UUID.randomUUID()).build())
                .userId(UUID.randomUUID())
                .orderDate(LocalDate.now())
                .status(OrderStatus.COMPLETED)
                .build();
        OrdersResponse response = new OrdersResponse(orderDomain);
        List<OrdersResponse> responseList = List.of(response);

        when(ordersController.findAll()).thenReturn(responseList);

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseList)));
    }

    @Test
    public void shouldFindAllOrdersPagedSuccessfully() throws Exception {
        Orders orderDomain = Orders.builder()
                .id(UUID.randomUUID())
                .eventId(Events.builder().id(UUID.randomUUID()).build())
                .userId(UUID.randomUUID())
                .orderDate(LocalDate.now())
                .status(OrderStatus.COMPLETED)
                .build();
        OrdersResponse response = new OrdersResponse(orderDomain);
        PagedResponse<OrdersResponse> pagedResponse = new PagedResponse<>(List.of(response), 1, 1, 0, 10);

        when(ordersController.findAllPaged(0, 10)).thenReturn(pagedResponse);

        mockMvc.perform(get("/orders/paged?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pagedResponse)));
    }

    @Test
    public void shouldDeleteOrderSuccessfully() throws Exception {
        UUID orderId = UUID.randomUUID();
        doNothing().when(ordersController).delete(orderId);

        mockMvc.perform(delete("/orders/" + orderId))
                .andExpect(status().isOk());

        verify(ordersController).delete(orderId);
    }

    @Test
    public void shouldReturn409WhenOptimisticLockingFailureOccurs() throws Exception {
        UUID orderId = UUID.randomUUID();
        when(ordersController.findById(orderId)).thenThrow(new ObjectOptimisticLockingFailureException(Object.class, orderId));

        mockMvc.perform(get("/orders/" + orderId))
                .andExpect(status().isConflict())
                .andExpect(content().string("The event was modified by another transaction. Please try again."));
    }
}
