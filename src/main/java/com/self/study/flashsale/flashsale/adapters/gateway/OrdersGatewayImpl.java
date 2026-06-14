package com.self.study.flashsale.flashsale.adapters.gateway;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.self.study.flashsale.flashsale.domain.models.Orders;
import com.self.study.flashsale.flashsale.domain.models.PagedResult;
import com.self.study.flashsale.flashsale.drivers.db.entities.OrdersEntity;
import com.self.study.flashsale.flashsale.drivers.db.repository.OrdersRepository;

@Component
public class OrdersGatewayImpl implements OrdersGateway {

    @Autowired
    private OrdersRepository ordersRepository;

    @Override
    public Orders save(Orders order) {
        return ordersRepository.save(order.toEntity()).toDomain();
    }

    @Override
    public Orders findById(UUID id) {
        return ordersRepository.findById(id).map(OrdersEntity::toDomain).orElse(null);
    }

    @Override
    public void delete(UUID id) {
        ordersRepository.deleteById(id);
    }

    @Override
    public List<Orders> findAll() {
        return ordersRepository.findAll().stream().map(OrdersEntity::toDomain).toList();
    }

    @Override
    public PagedResult<Orders> findAllPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrdersEntity> pageResult = ordersRepository.findAll(pageable);
        List<Orders> content = pageResult.getContent().stream()
                .map(OrdersEntity::toDomain)
                .toList();
        return new PagedResult<>(content, pageResult.getTotalElements(), pageResult.getTotalPages(), page, size);
    }
}
