package com.gskart.order.data.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Entity(name = "contacts")
public class Contact extends BaseEntity {
    @Column(length = 64)
    private String firstName;

    @Column(length = 64)
    private String lastName;

    @ElementCollection
    @Column(length = 160)
    private Set<String> emailIds;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<PhoneNumber> phoneNumbers;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Address> addresses;

    @Enumerated(EnumType.ORDINAL)
    private Type type;

    public enum Type{
        BILLING,
        SHIPPING,
        SECONDARY
    }
}
