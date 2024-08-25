package com.gskart.order.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
@Entity(name = "phoneNumbers")
public class PhoneNumber extends BaseEntity {
    @Column(length = 16)
    private String number;

    @Enumerated(EnumType.ORDINAL)
    private NumberType type;

    @Column(length = 8)
    private String countryCode;

    public enum NumberType {
        MOBILE,
        PHONE
    }
}

