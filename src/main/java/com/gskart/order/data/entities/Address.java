package com.gskart.order.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity(name = "addresses")
public class Address extends BaseEntity {
    @Column(length = 16)
    private String doorNumber;

    @Column(length = 256)
    private String street;

    @Column(length = 512)
    private String line1;

    @Column(length = 512)
    private String line2;

    @Column(length = 196)
    private String city;

    @Column(length = 196)
    private String state;

    @Column(length = 12)
    private String zip;

    @Column(length = 196)
    private String country;
    private Type type;

    public enum Type {
        PERMANENT,
        TEMPORARY
    }
}
