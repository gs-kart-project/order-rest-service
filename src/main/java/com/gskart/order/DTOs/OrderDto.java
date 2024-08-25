package com.gskart.order.DTOs;

import com.gskart.order.data.entities.Order;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class OrderDto {
    private String orderId;
    private String placedBy;
    private OffsetDateTime placedOn;
    private Order.Status status;
    private List<OrderedItemDto> orderedItems;
    private List<PaymentDetailDto> paymentDetails;
    private List<DeliveryDetailDto> deliveryDetails;
}
