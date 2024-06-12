package com.gskart.order.mappers;

import com.gskart.order.DTOs.requests.OrderRequest;
import com.gskart.order.data.entities.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public Order orderRequestToOrderEntity(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderedItems(orderRequest.getOrderedItems());
        order.setPlacedBy(orderRequest.getPlacedBy());
        order.setPlacedOn(orderRequest.getPlacedOn());
        order.setPaymentDetails(orderRequest.getPaymentDetails());
        return order;
    }
}
