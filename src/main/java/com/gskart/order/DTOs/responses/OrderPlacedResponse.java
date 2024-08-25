package com.gskart.order.DTOs.responses;

import lombok.Data;

@Data
public class OrderPlacedResponse extends ErrorResponse {
    private String orderId;
    private String orderStatus;
}
