package com.trendafilov.ivan.worldpay.offerservice.dtos.response;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(description = "Simple Message Response")
public class ErrorResponse {

    private String message;
}
