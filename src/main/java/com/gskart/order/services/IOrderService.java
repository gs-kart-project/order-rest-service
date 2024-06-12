package com.gskart.order.services;

import com.gskart.order.data.entities.Order;

public interface IOrderService {
    Order saveOrder(Order order);
}
