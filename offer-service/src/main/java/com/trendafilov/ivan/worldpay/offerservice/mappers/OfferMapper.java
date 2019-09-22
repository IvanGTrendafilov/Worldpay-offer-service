package com.trendafilov.ivan.worldpay.offerservice.mappers;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.OfferRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.MerchantResponse;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.OfferResponse;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.ProductItemResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Merchant;
import com.trendafilov.ivan.worldpay.offerservice.entities.Offer;
import com.trendafilov.ivan.worldpay.offerservice.enums.ErrorMessagesEnum;
import com.trendafilov.ivan.worldpay.offerservice.enums.OfferStatus;
import com.trendafilov.ivan.worldpay.offerservice.exceptions.OfferServiceException;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
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
                                                final Merchant merchant)
        throws OfferServiceException {
        final BigDecimal price = offerRequest.getPrice();
        if (price.intValue() < 0) {
            log.error("Merchant with id: {}", merchant.getMerchantId(),
                      " trying to insert price less than zero");
            throw new OfferServiceException(ErrorMessagesEnum.PRICE_LESS_THAN_ZERO.getMessage(),
                                            HttpStatus.BAD_REQUEST.value());
        }
        final Offer
            offer =
            Offer.builder()
                 .merchant(merchant)
                 .description(offerRequest.getDescription())
                 .price(price)
                 .status(OfferStatus.ACTIVE.toString())
                 .currency(offerRequest.getCurrency())
                 .expireDate(DateUtils.addDays(new Date(), 10))
                 .build();
        return offer;
    }
}
