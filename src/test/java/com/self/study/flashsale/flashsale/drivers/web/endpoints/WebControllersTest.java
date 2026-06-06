package com.self.study.flashsale.flashsale.drivers.web.endpoints;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.self.study.flashsale.flashsale.adapters.controllers.EventsController;
import com.self.study.flashsale.flashsale.adapters.controllers.OrdersController;
import com.self.study.flashsale.flashsale.drivers.messaging.producer.OrdersProducer;

@WebMvcTest(controllers = {EventWebController.class, OrdersWebController.class})
public class WebControllersTest {

    @Autowired
    private MockMvc mockMvc;

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
}
