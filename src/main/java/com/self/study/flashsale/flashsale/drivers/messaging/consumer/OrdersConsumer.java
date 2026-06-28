package com.self.study.flashsale.flashsale.drivers.messaging.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache;

import com.self.study.flashsale.flashsale.adapters.controllers.OrdersController;
import com.self.study.flashsale.flashsale.adapters.controllers.EventsController;
import com.self.study.flashsale.flashsale.adapters.presenters.OrdersRequest;
import com.self.study.flashsale.flashsale.adapters.presenters.OrdersResponse;
import com.self.study.flashsale.flashsale.adapters.presenters.EventResponse;

import io.micrometer.core.instrument.MeterRegistry;
import java.util.UUID;

@Service
public class OrdersConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrdersConsumer.class);

    private final OrdersController ordersController;
    private final CacheManager cacheManager;
    private final EventsController eventsController;
    private final MeterRegistry meterRegistry;

    public OrdersConsumer(OrdersController ordersController, CacheManager cacheManager,
                          EventsController eventsController, MeterRegistry meterRegistry) {
        this.ordersController = ordersController;
        this.cacheManager = cacheManager;
        this.eventsController = eventsController;
        this.meterRegistry = meterRegistry;
    }

    @KafkaListener(topics = "orders-topic", groupId = "orders-group")
    public void consumeOrderRequest(OrdersRequest ordersRequest) {
        UUID eventId = ordersRequest.getEventId();
        String eventIdStr = eventId != null ? eventId.toString() : "unknown";
        String eventName = getEventName(eventId);

        // Count request made
        meterRegistry.counter("orders_requests_total", 
            "event_id", eventIdStr, 
            "event_name", eventName
        ).increment();

        logger.info("Order request received: Event ID={}, Event Name={}, Order ID={}", 
            eventIdStr, eventName, ordersRequest.getId());

        try {
            OrdersResponse response = ordersController.save(ordersRequest);
            
            // Get actual event name from response on success if it was unknown
            if ("unknown".equals(eventName) && response.getEventId() != null) {
                eventName = response.getEventId().getName();
            }

            // Count success
            meterRegistry.counter("orders_placed_total", 
                "event_id", eventIdStr, 
                "event_name", eventName
            ).increment();

            logger.info("Order placed successfully: Event ID={}, Event Name={}, Order ID={}", 
                eventIdStr, eventName, ordersRequest.getId());

            Cache ordersAllCache = cacheManager.getCache("orders_all");
            if (ordersAllCache != null) {
                ordersAllCache.clear();
            }
            Cache ordersPagedCache = cacheManager.getCache("orders_paged");
            if (ordersPagedCache != null) {
                ordersPagedCache.clear();
            }
        } catch (Exception e) {
            String reason = "error";
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("full")) {
                reason = "out_of_capacity";
            }

            // Count failure
            meterRegistry.counter("orders_failed_total", 
                "event_id", eventIdStr, 
                "event_name", eventName, 
                "reason", reason
            ).increment();

            logger.error("Failed to place order: Event ID={}, Event Name={}, Reason={}, Error={}", 
                eventIdStr, eventName, reason, e.getMessage());
        }
    }

    private String getEventName(UUID eventId) {
        if (eventId == null) {
            return "unknown";
        }
        try {
            Cache cache = cacheManager.getCache("events");
            if (cache != null) {
                Cache.ValueWrapper wrapper = cache.get(eventId);
                if (wrapper != null) {
                    Object val = wrapper.get();
                    if (val instanceof EventResponse) {
                        return ((EventResponse) val).getName();
                    }
                }
            }
            EventResponse event = eventsController.findById(eventId);
            return event != null ? event.getName() : "unknown";
        } catch (Exception e) {
            return "unknown";
        }
    }
}

