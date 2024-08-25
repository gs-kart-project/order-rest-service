package com.gskart.order.data.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "paymentDetails")
public class PaymentDetail extends BaseEntity {
    private String paymentId;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Contact billContact;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    public enum Status {
        INITIATED,
        PENDING,
        PARTIALLY_COMPLETED,
        COMPLETED
    }
}
