package com.gskart.order.data.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity(name = "paymentDetails")
public class PaymentDetail extends BaseEntity {

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Contact billContact;
    private Status status;

    public enum Status {
        INITIATED,
        PENDING,
        PARTIALLY_COMPLETED,
        COMPLETED
    }
}
