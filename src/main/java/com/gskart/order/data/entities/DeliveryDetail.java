package com.gskart.order.data.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Entity(name = "deliveryDetails")
public class DeliveryDetail extends BaseEntity{

    @OneToMany(mappedBy = "deliveryDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderedItem> orderedItems;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Contact> contacts;

    private OffsetDateTime deliveredOn;
    private OffsetDateTime shippedOn;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    public enum Status {
        NOT_INITIATED,
        INITIATED,
        SHIPPED,
        DELIVERED
    }
}
