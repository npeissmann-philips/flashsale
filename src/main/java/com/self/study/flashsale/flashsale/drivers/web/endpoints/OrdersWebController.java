package com.self.study.flashsale.flashsale.drivers.web.endpoints;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.self.study.flashsale.flashsale.adapters.controllers.OrdersController;
import com.self.study.flashsale.flashsale.adapters.presenters.OrdersRequest;
import com.self.study.flashsale.flashsale.adapters.presenters.OrdersResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders")
public class OrdersWebController {

    OrdersController ordersController;

    public OrdersWebController(OrdersController ordersController) {
        this.ordersController = ordersController;
    }

    @PostMapping
    @Operation(summary = "Save an order")
    public OrdersResponse save(@RequestBody OrdersRequest ordersRequest) {
        return ordersController.save(ordersRequest);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find an order by id")
    public OrdersResponse findById(@PathVariable UUID id) {
        return ordersController.findById(id);
    }

    @GetMapping
    @Operation(summary = "Find all orders")
    public List<OrdersResponse> findAll() {
        return ordersController.findAll();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an order by id")
    public void delete(@PathVariable UUID id) {
        ordersController.delete(id);
    }
}
