package com.gskart.order.DTOs;

import lombok.Data;

@Data
public class PhoneNumberDto {
    String id;
    private String number;
    private String type;
    private String countryCode;
}
