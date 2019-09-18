package com.trendafilov.ivan.worldpay.offerservice.services.impl;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.response.MerchantResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Merchant;
import com.trendafilov.ivan.worldpay.offerservice.enums.ErrorMessagesEnum;
import com.trendafilov.ivan.worldpay.offerservice.exceptions.OfferServiceException;
import com.trendafilov.ivan.worldpay.offerservice.mappers.MerchantMapper;
import com.trendafilov.ivan.worldpay.offerservice.repositories.MerchantRepository;
import com.trendafilov.ivan.worldpay.offerservice.services.IMerchantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MerchantService implements IMerchantService {

    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;

    @Autowired
    public MerchantService(
        final MerchantRepository merchantRepository,
        final MerchantMapper merchantMapper) {
        this.merchantRepository = merchantRepository;
        this.merchantMapper = merchantMapper;
    }

    @Override
    public Merchant findMerchantByMerchantId(final String merchantId) throws OfferServiceException {
        long providedMerchantId = 0L;
        Merchant merchant = null;
        try {
            providedMerchantId = Long.parseLong(merchantId);
            final Optional<Merchant>
                merchantOptional =
                merchantRepository.findById(providedMerchantId);
            if (!merchantOptional.isPresent()) {
                return throwOfferServiceException();
            }
            merchant = merchantOptional.get();
        } catch (final NumberFormatException e) {
            throwOfferServiceException();
        }
        return merchant;
    }

    private Merchant throwOfferServiceException() throws OfferServiceException {
        throw new OfferServiceException(ErrorMessagesEnum.MERCHANT_NOT_FOUND.getMessage(),
                                        HttpStatus.BAD_REQUEST.value());
    }

    @Override
    public MerchantResponse getMerchantResponseByMerchantEntity(final Merchant merchant) {
        return merchantMapper.convertMerchantToResponse(
            merchant);
    }
}
