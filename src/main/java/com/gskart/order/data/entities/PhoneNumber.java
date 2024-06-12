package com.gskart.order.data.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity(name = "phoneNumbers")
public class PhoneNumber extends BaseEntity {
    private String number;
    private NumberType type;
    private String countryCode;
    public enum NumberType {
        MOBILE,
        PHONE
    }
}

