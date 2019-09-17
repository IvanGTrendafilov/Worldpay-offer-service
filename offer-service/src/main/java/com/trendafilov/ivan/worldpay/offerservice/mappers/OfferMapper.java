package com.trendafilov.ivan.worldpay.offerservice.mappers;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.response.MerchantResponse;
import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.response.OfferResponse;
import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.response.ProductItemResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Offer;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OfferMapper {

    public OfferResponse convertFullOfferToResponse(final Offer offer,
                                                    final List<ProductItemResponse> productItemResponses,
                                                    final MerchantResponse merchantResponse) {
        return OfferResponse.builder()
                            .productItemResponses(productItemResponses)
                            .currency(offer.getCurrency())
                            .description(offer.getDescription())
                            .merchantResponse(merchantResponse)
                            .expireDate(offer.getExpireDate())
                            .offerId(offer.getOfferId())
                            .price(offer.getPrice())
                            .status(offer.getStatus())
                            .build();
    }
}
