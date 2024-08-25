package com.gskart.order.DTOs;

import com.gskart.order.data.entities.Address;
import com.gskart.order.data.entities.PhoneNumber;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ContactDto {
    String id;
    private String firstName;
    private String lastName;
    private Set<String> emailIds;
    List<PhoneNumberDto> phoneNumbers;
    List<AddressDto> addresses;
    private String type;
}
