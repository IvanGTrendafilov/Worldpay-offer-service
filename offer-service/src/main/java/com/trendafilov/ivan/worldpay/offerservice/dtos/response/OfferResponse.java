package com.trendafilov.ivan.worldpay.offerservice.dtos.response;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OfferResponse {

    private MerchantResponse merchantResponse;
    private Long offerId;
    private String description;
    private Date expireDate;
    private String status;
    private String currency;
    private BigDecimal price;
    private StudentResponse studentResponse;
    private List<ProductItemResponse> productItemResponses;
}
