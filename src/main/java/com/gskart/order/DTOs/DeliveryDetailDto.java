package com.gskart.order.DTOs;

import com.gskart.order.data.entities.Contact;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class DeliveryDetailDto {
    String id;
    private List<ContactDto> contacts;
    private OffsetDateTime deliveredOn;
    private OffsetDateTime shippedOn;
    private List<Integer> productIds;
    String status;
}
