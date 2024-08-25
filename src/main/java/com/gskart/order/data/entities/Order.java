package com.gskart.order.data.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Entity(name = "orders")
public class Order extends BaseEntity{
    @Column(length = 160)
    private String placedBy;
    private OffsetDateTime placedOn;

    @Column(length = 36)
    private String cartId;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<OrderedItem> orderedItems;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<PaymentDetail> paymentDetails;

    public enum Status {
        CREATED,
        CONFIRMED,
        SHIPPED,
        DELIVERED
    }
}

