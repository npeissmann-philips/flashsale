package com.self.study.flashsale.flashsale.drivers.messaging.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.self.study.flashsale.flashsale.adapters.controllers.OrdersController;
import com.self.study.flashsale.flashsale.adapters.presenters.OrdersRequest;

@Service
public class OrdersConsumer {

    private final OrdersController ordersController;

    public OrdersConsumer(OrdersController ordersController) {
        this.ordersController = ordersController;
    }

    @KafkaListener(topics = "orders-topic", groupId = "orders-group")
    public void consumeOrderRequest(OrdersRequest ordersRequest) {
        try {
            ordersController.save(ordersRequest);
        } catch (Exception e) {
            System.err.println("Failed to process order: " + e.getMessage());
        }
    }
}
