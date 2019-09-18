package com.trendafilov.ivan.worldpay.offerservice.mappers;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.MerchantRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.MerchantResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Merchant;

import org.springframework.stereotype.Component;

@Component
public class MerchantMapper {

    public MerchantResponse convertMerchantToResponse(final Merchant merchant) {
        return MerchantResponse.builder()
                               .department(merchant.getDepartment())
                               .merchantId(merchant.getMerchantId())
                               .firstName(merchant.getFirstName())
                               .lastName(merchant.getLastName())
                               .build();
    }

    public Merchant convertMerchantRequestToEntity(final MerchantRequest merchantRequest) {
        return Merchant.builder()
                       .firstName(merchantRequest.getFirstName())
                       .lastName(merchantRequest.getLastName())
                       .department(merchantRequest.getDepartment())
                       .build();
    }

}
