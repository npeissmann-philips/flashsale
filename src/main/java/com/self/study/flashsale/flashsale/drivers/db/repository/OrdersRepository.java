package com.self.study.flashsale.flashsale.drivers.db.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.self.study.flashsale.flashsale.drivers.db.entities.OrdersEntity;

public interface OrdersRepository extends JpaRepository<OrdersEntity, UUID> {

}
