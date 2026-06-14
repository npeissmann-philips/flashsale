package com.self.study.flashsale.flashsale.adapters.controllers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Component;

import com.self.study.flashsale.flashsale.adapters.presenters.OrdersRequest;
import com.self.study.flashsale.flashsale.adapters.presenters.OrdersResponse;
import com.self.study.flashsale.flashsale.adapters.presenters.PagedResponse;
import com.self.study.flashsale.flashsale.application.useCase.orders.DeleteOrder;
import com.self.study.flashsale.flashsale.application.useCase.orders.FindAllOrders;
import com.self.study.flashsale.flashsale.application.useCase.orders.FindAllOrdersPaged;
import com.self.study.flashsale.flashsale.application.useCase.orders.FindOrderById;
import com.self.study.flashsale.flashsale.application.useCase.orders.SaveOrder;
import com.self.study.flashsale.flashsale.domain.models.Orders;
import com.self.study.flashsale.flashsale.domain.models.PagedResult;

@Component
public class OrdersControllerImpl implements OrdersController {

    @Autowired
    private SaveOrder saveOrder;
    @Autowired
    private FindOrderById findOrder;
    @Autowired
    private FindAllOrders findAllOrders;
    @Autowired
    private FindAllOrdersPaged findAllOrdersPaged;
    @Autowired
    private DeleteOrder deleteOrder;

    public OrdersControllerImpl(SaveOrder saveOrder, FindOrderById findOrder, FindAllOrders findAllOrders,
            FindAllOrdersPaged findAllOrdersPaged, DeleteOrder deleteOrder) {
        this.saveOrder = saveOrder;
        this.findOrder = findOrder;
        this.findAllOrders = findAllOrders;
        this.findAllOrdersPaged = findAllOrdersPaged;
        this.deleteOrder = deleteOrder;
    }

    @Override
    public OrdersResponse save(OrdersRequest orderRequest) {
        return new OrdersResponse(saveOrder.execute(orderRequest.toDomain()));
    }

    @Override
    public OrdersResponse findById(UUID id) throws NotFoundException{
        Orders order = findOrder.execute(id);
        if(order == null){
            throw new NotFoundException();
        }

        return new OrdersResponse(order);
    }

    @Override
    public List<OrdersResponse> findAll() {
        return findAllOrders.execute().stream().map(OrdersResponse::new).collect(Collectors.toList());
    }

    @Override
    public PagedResponse<OrdersResponse> findAllPaged(int page, int size) {
        PagedResult<Orders> pagedResult = findAllOrdersPaged.execute(page, size);
        List<OrdersResponse> content = pagedResult.getContent().stream()
                .map(OrdersResponse::new)
                .toList();
        return new PagedResponse<>(
                content,
                pagedResult.getTotalElements(),
                pagedResult.getTotalPages(),
                pagedResult.getPageNumber(),
                pagedResult.getPageSize()
        );
    }

    @Override
    public void delete(UUID id) {
        deleteOrder.execute(id);
    }
}
