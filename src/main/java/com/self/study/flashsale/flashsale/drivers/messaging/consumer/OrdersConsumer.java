package com.self.study.flashsale.flashsale.drivers.messaging.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache;

import com.self.study.flashsale.flashsale.adapters.controllers.OrdersController;
import com.self.study.flashsale.flashsale.adapters.presenters.OrdersRequest;

@Service
public class OrdersConsumer {

    private final OrdersController ordersController;
    private final CacheManager cacheManager;

    public OrdersConsumer(OrdersController ordersController, CacheManager cacheManager) {
        this.ordersController = ordersController;
        this.cacheManager = cacheManager;
    }

    @KafkaListener(topics = "orders-topic", groupId = "orders-group")
    public void consumeOrderRequest(OrdersRequest ordersRequest) {
        try {
            ordersController.save(ordersRequest);
            Cache ordersAllCache = cacheManager.getCache("orders_all");
            if (ordersAllCache != null) {
                ordersAllCache.clear();
            }
            Cache ordersPagedCache = cacheManager.getCache("orders_paged");
            if (ordersPagedCache != null) {
                ordersPagedCache.clear();
            }
        } catch (Exception e) {
            System.err.println("Failed to process order: " + e.getMessage());
        }
    }
}
