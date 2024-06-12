package com.gskart.order.data.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.util.List;

@Data
@Entity(name = "orderedItems")
public class OrderedItem extends BaseEntity {
    Integer productId;
    String productName;
    String description;
    Float quantity;
    Double unitPrice;
    Double totalPrice;
    QuantityUnit quantityUnit;

    @ManyToMany(mappedBy = "orderedItems",cascade = CascadeType.ALL)
    List<DeliveryDetail> deliveryDetails;

    public enum QuantityUnit {
        COUNT,
        KILO_GRAMS
    }
}
