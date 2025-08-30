package com.mpesa.sampleapi.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StkPushApiRequest {
    private String phone;
    private String amount;
    private String accountReference;

}