package com.gskart.order.DTOs.responses;

import lombok.Data;

@Data
public class OrderResponse extends ErrorResponse {
    private String orderId;
    private String status;
}
