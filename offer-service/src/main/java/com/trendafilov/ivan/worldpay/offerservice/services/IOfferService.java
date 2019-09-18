package com.trendafilov.ivan.worldpay.offerservice.services;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.OfferRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.OfferResponse;
import com.trendafilov.ivan.worldpay.offerservice.exceptions.OfferServiceException;

import java.util.List;

public interface IOfferService {

    /**
     * Save {@link com.trendafilov.ivan.worldpay.offerservice.entities.Offer} into database by
     * request
     *
     * @param merchantId
     * @param offerRequest
     * @return {@link OfferResponse}
     * @throws OfferServiceException If there isn't such merchant into database
     */
    OfferResponse insertOfferForMerchant(final String merchantId,
                                         final OfferRequest offerRequest) throws
                                                                          OfferServiceException;

    /**
     * Get all active offers for merchant
     *
     * @param merchantId
     * @return List of {@link OfferResponse}
     * @throws OfferServiceException If there isn't such merchant into database
     */
    List<OfferResponse> getActiveOffersForMerchant(String merchantId) throws OfferServiceException;

    /**
     * Makes an {@link com.trendafilov.ivan.worldpay.offerservice.entities.Offer} inactive
     *
     * @param merchantId
     * @param offerId
     * @throws OfferServiceException If there isn't such merchant into database or there isn't such
     *                               offer into db
     */
    void cancelMerchantOffer(String merchantId, String offerId) throws OfferServiceException;
}
