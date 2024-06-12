package com.gskart.order.data.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Entity(name = "orders")
public class Order extends BaseEntity{
    private String placedBy;
    private OffsetDateTime placedOn;
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
