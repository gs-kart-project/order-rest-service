package com.gskart.order.data.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
@Entity(name = "contacts")
public class Contact extends BaseEntity {
    private String firstName;
    private String lastName;
    private String emailId;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<PhoneNumber> phoneNumbers;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Address> addresses;

    private Type type;

    public enum Type{
        BILLING,
        SHIPPING,
        SECONDARY
    }
}
