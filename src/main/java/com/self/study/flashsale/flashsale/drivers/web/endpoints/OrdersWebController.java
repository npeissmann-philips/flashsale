package com.self.study.flashsale.flashsale.drivers.web.endpoints;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import com.self.study.flashsale.flashsale.adapters.controllers.OrdersController;
import com.self.study.flashsale.flashsale.adapters.presenters.OrdersRequest;
import com.self.study.flashsale.flashsale.adapters.presenters.OrdersResponse;
import com.self.study.flashsale.flashsale.adapters.presenters.PagedResponse;

import com.self.study.flashsale.flashsale.drivers.messaging.producer.OrdersProducer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders")
public class OrdersWebController {

    private final OrdersController ordersController;
    private final OrdersProducer ordersProducer;

    public OrdersWebController(OrdersController ordersController, OrdersProducer ordersProducer) {
        this.ordersController = ordersController;
        this.ordersProducer = ordersProducer;
    }

    @PostMapping
    @Operation(summary = "Save an order")
    public ResponseEntity<String> save(@RequestBody OrdersRequest ordersRequest) {
        ordersProducer.sendOrderRequest(ordersRequest);
        return ResponseEntity.accepted().body("Order request accepted for processing");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find an order by id")
    @Cacheable(value = "orders", key = "#id")
    public OrdersResponse findById(@PathVariable UUID id) throws NotFoundException{
        return ordersController.findById(id);
    }

    @GetMapping
    @Operation(summary = "Find all orders")
    @Cacheable(value = "orders_all", key = "'list'")
    public List<OrdersResponse> findAll() {
        return ordersController.findAll();
    }

    @GetMapping("/paged")
    @Operation(summary = "Find all orders paged")
    @Cacheable(value = "orders_paged", key = "#page + '-' + #size")
    public PagedResponse<OrdersResponse> findAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ordersController.findAllPaged(page, size);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an order by id")
    @Caching(evict = {
        @CacheEvict(value = "orders", key = "#id"),
        @CacheEvict(value = "orders_all", allEntries = true),
        @CacheEvict(value = "orders_paged", allEntries = true)
    })
    public void delete(@PathVariable UUID id) {
        ordersController.delete(id);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<String> handleOptimisticLockingFailure(ObjectOptimisticLockingFailureException e) {
        return ResponseEntity.status(409).body("The event was modified by another transaction. Please try again.");
    }
}
