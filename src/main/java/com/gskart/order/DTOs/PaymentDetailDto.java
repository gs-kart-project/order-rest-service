package com.gskart.order.DTOs;

import com.gskart.order.data.entities.Contact;
import lombok.Data;

@Data
public class PaymentDetailDto {
    String id;
    private String paymentId;
    private ContactDto billContact;
    private String status;
}
