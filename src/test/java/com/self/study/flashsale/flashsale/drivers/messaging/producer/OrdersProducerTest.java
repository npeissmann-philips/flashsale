package com.self.study.flashsale.flashsale.drivers.messaging.producer;

import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import com.self.study.flashsale.flashsale.adapters.presenters.OrdersRequest;
import com.self.study.flashsale.flashsale.drivers.db.entities.enums.OrderStatus;

@ExtendWith(MockitoExtension.class)
class OrdersProducerTest {

    @Mock
    private KafkaTemplate<Object, Object> kafkaTemplate;

    @InjectMocks
    private OrdersProducer ordersProducer;

    @Test
    void shouldSendOrderRequest() {
        OrdersRequest request = new OrdersRequest(UUID.randomUUID(), UUID.randomUUID(), LocalDate.now(), OrderStatus.PENDING, UUID.randomUUID());

        ordersProducer.sendOrderRequest(request);

        verify(kafkaTemplate).send("orders-topic", request);
    }
}
