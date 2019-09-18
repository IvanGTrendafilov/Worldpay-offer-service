package com.trendafilov.ivan.worldpay.offerservice.services;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.OfferRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.response.OfferResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Offer;
import com.trendafilov.ivan.worldpay.offerservice.exceptions.OfferServiceException;

import java.util.List;

public interface IOfferService {

    List<Offer> findAllOffersInStore();

    OfferResponse insertOfferForMerchant(final String merchantId,
                                         final OfferRequest offerRequest) throws
                                                                          OfferServiceException;

    List<OfferResponse> getActiveOffersForMerchant(String merchantId) throws OfferServiceException;
}
