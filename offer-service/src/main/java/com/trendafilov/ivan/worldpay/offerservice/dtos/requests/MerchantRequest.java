package com.trendafilov.ivan.worldpay.offerservice.dtos.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MerchantRequest {

    private String firstName;
    private String lastName;
    private String department;
}
