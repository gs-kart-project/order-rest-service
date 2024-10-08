package com.gskart.order.DTOs.requests;

import com.gskart.order.DTOs.DeliveryDetailDto;
import com.gskart.order.DTOs.OrderedItemDto;
import com.gskart.order.DTOs.PaymentDetailDto;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class OrderRequest {
    private String placedBy;
    private OffsetDateTime placedOn;
    private List<OrderedItemDto> orderedItems;
    private List<PaymentDetailDto> paymentDetails;
    private List<DeliveryDetailDto> deliveryDetails;
    private String cartId;
}
