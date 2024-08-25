package com.gskart.order.data.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity(name = "orderedItems")
public class OrderedItem extends BaseEntity {
    Integer productId;

    @Column(length = 64)
    String productName;

    @Column(length = 1024)
    String description;
    Float quantity;
    Double unitPrice;
    Double totalPrice;

    @Enumerated(EnumType.ORDINAL)
    QuantityUnit quantityUnit;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    DeliveryDetail deliveryDetails;

    public enum QuantityUnit {
        COUNT,
        KILO_GRAMS
    }
}
