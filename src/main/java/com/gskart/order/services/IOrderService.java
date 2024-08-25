package com.gskart.order.services;

import com.gskart.order.data.entities.DeliveryDetail;
import com.gskart.order.data.entities.Order;
import com.gskart.order.data.entities.PaymentDetail;

import java.util.List;

public interface IOrderService {
    Order saveOrder(Order order);

    Order savePaymentDetails(String orderId, List<PaymentDetail> paymentDetails);

    List<DeliveryDetail> saveDeliveryDetails(List<DeliveryDetail> deliveryDetails);

    List<Order> getOrdersByUserPlacedBy(String username);

    Order getOrderById(String id);
}
