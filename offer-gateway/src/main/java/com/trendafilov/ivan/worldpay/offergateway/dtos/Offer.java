package com.trendafilov.ivan.worldpay.offergateway.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Offer {

    private Long offerId;
    private String description;
}
