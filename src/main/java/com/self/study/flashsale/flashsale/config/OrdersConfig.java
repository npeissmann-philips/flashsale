package com.self.study.flashsale.flashsale.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.self.study.flashsale.flashsale.adapters.gateway.OrdersGateway;
import com.self.study.flashsale.flashsale.application.useCase.orders.DeleteOrder;
import com.self.study.flashsale.flashsale.application.useCase.orders.DeleteOrderImpl;
import com.self.study.flashsale.flashsale.application.useCase.orders.FindAllOrders;
import com.self.study.flashsale.flashsale.application.useCase.orders.FindAllOrdersImpl;
import com.self.study.flashsale.flashsale.application.useCase.orders.FindOrderById;
import com.self.study.flashsale.flashsale.application.useCase.orders.FindOrderByIdImpl;
import com.self.study.flashsale.flashsale.application.useCase.orders.SaveOrder;
import com.self.study.flashsale.flashsale.application.useCase.orders.SaveOrderImpl;

@Configuration
public class OrdersConfig {

    @Bean
    public SaveOrder saveOrder(OrdersGateway ordersGateway) {
        return new SaveOrderImpl(ordersGateway);
    }

    @Bean
    public FindOrderById findOrderById(OrdersGateway ordersGateway) {
        return new FindOrderByIdImpl(ordersGateway);
    }

    @Bean
    public FindAllOrders findAllOrders(OrdersGateway ordersGateway) {
        return new FindAllOrdersImpl(ordersGateway);
    }

    @Bean
    public DeleteOrder deleteOrder(OrdersGateway ordersGateway) {
        return new DeleteOrderImpl(ordersGateway);
    }
}
