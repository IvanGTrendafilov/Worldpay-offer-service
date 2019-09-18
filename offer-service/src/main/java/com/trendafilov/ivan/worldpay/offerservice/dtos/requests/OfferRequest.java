package com.trendafilov.ivan.worldpay.offerservice.dtos.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfferRequest {

    private String description;
    private String status;
    private String currency;
    private BigDecimal price;
    private List<ProductItemRequest> productItemRequests;
}
