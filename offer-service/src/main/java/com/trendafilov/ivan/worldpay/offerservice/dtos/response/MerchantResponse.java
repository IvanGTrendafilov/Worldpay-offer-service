package com.trendafilov.ivan.worldpay.offerservice.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MerchantResponse {

    private Long merchantId;
    private String firstName;
    private String lastName;
    private String department;
}
