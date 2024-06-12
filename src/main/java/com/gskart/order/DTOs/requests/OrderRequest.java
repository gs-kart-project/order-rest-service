package com.gskart.order.DTOs.requests;

import com.gskart.order.data.entities.Order;
import com.gskart.order.data.entities.OrderedItem;
import com.gskart.order.data.entities.PaymentDetail;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class OrderRequest {
    private String placedBy;
    private OffsetDateTime placedOn;
    private List<OrderedItem> orderedItems;
    private List<PaymentDetail> paymentDetails;
}
