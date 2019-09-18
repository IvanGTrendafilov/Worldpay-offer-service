package com.trendafilov.ivan.worldpay.offerservice.services;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.OfferRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.OfferResponse;
import com.trendafilov.ivan.worldpay.offerservice.exceptions.OfferServiceException;

import java.util.List;

public interface IOfferService {

    OfferResponse insertOfferForMerchant(final String merchantId,
                                         final OfferRequest offerRequest) throws
                                                                          OfferServiceException;

    List<OfferResponse> getActiveOffersForMerchant(String merchantId) throws OfferServiceException;

    void cancelMerchantOffer(String merchantId, String offerId) throws OfferServiceException;
}
