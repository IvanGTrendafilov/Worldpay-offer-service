package com.trendafilov.ivan.worldpay.offerservice.services;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.MerchantRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.MerchantResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Merchant;
import com.trendafilov.ivan.worldpay.offerservice.exceptions.OfferServiceException;

import java.util.List;

public interface IMerchantService {

    /**
     * Get Merchant by Id
     *
     * @param merchantId
     * @return {@link Merchant}
     * @throws OfferServiceException When merchant id is not provided or there isn't such merchant
     *                               into database
     */
    Merchant findMerchantByMerchantId(String merchantId) throws OfferServiceException;

    /**
     * Get Merchant response object
     *
     * @param merchant
     * @return {@link MerchantResponse}
     */
    MerchantResponse getMerchantResponseByMerchantEntity(Merchant merchant);

    /**
     * Get all merchants into database
     *
     * @return List with all merchants into database
     */
    List<MerchantResponse> getAllMerchants();

    /**
     * Create {@link Merchant} and insert it into database
     *
     * @param merchantRequest {@link MerchantRequest}
     * @return {@link MerchantResponse}
     * @throws OfferServiceException when first name or last name is not provided
     */
    MerchantResponse createMerchantByMerchantRequest(MerchantRequest merchantRequest)
        throws OfferServiceException;
}
