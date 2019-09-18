package com.trendafilov.ivan.worldpay.offerservice.services;

import com.trendafilov.ivan.worldpay.offerservice.dtos.response.MerchantResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Merchant;
import com.trendafilov.ivan.worldpay.offerservice.exceptions.OfferServiceException;

public interface IMerchantService {

    Merchant findMerchantByMerchantId(String merchantId) throws OfferServiceException;

    MerchantResponse getMerchantResponseByMerchantEntity(Merchant merchant);
}
