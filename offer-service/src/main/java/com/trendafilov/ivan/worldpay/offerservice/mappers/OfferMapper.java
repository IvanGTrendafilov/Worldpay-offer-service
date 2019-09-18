package com.trendafilov.ivan.worldpay.offerservice.mappers;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.OfferRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.response.MerchantResponse;
import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.response.OfferResponse;
import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.response.ProductItemResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Merchant;
import com.trendafilov.ivan.worldpay.offerservice.entities.Offer;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
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

    public Offer convertOfferRequestToJpaEntity(final OfferRequest offerRequest,
                                                final Merchant merchant) {
        final Offer
            offer =
            Offer.builder()
                 .merchant(merchant)
                 .description(offerRequest.getDescription())
                 .price(offerRequest.getPrice())
                 .status(offerRequest.getStatus())
                 .currency(offerRequest.getCurrency())
                 .expireDate(DateUtils.addDays(new Date(), 10))
                 .build();
        return offer;
    }
}
