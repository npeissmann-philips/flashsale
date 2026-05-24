package com.self.study.flashsale.flashsale.drivers.messaging.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.self.study.flashsale.flashsale.adapters.presenters.OrdersRequest;

@Service
public class OrdersProducer {

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    public OrdersProducer(KafkaTemplate<Object, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderRequest(OrdersRequest ordersRequest) {
        kafkaTemplate.send("orders-topic", ordersRequest);
    }
}
