package com.gskart.order.data.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Entity(name = "deliveryDetails")
public class DeliveryDetail extends BaseEntity{

    @ManyToMany(cascade = CascadeType.ALL)
    private List<OrderedItem> orderedItems;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Contact> contacts;

    private OffsetDateTime deliveredOn;
    private OffsetDateTime shippedOn;
    private Status status;

    public enum Status {
        NOT_INITIATED,
        INITIATED,
        SHIPPED,
        DELIVERED
    }
}
