package com.gskart.order.data.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity(name = "addresses")
public class Address extends BaseEntity {
    private String doorNumber;
    private String street;
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String zip;
    private String country;
}
