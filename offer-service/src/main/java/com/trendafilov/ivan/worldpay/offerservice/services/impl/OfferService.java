package com.trendafilov.ivan.worldpay.offerservice.services.impl;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.OfferRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.ProductItemRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.MerchantResponse;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.OfferResponse;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.ProductItemResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Merchant;
import com.trendafilov.ivan.worldpay.offerservice.entities.Offer;
import com.trendafilov.ivan.worldpay.offerservice.enums.ErrorMessagesEnum;
import com.trendafilov.ivan.worldpay.offerservice.enums.OfferStatus;
import com.trendafilov.ivan.worldpay.offerservice.exceptions.OfferServiceException;
import com.trendafilov.ivan.worldpay.offerservice.mappers.OfferMapper;
import com.trendafilov.ivan.worldpay.offerservice.repositories.OfferRepository;
import com.trendafilov.ivan.worldpay.offerservice.services.IMerchantService;
import com.trendafilov.ivan.worldpay.offerservice.services.IOfferService;
import com.trendafilov.ivan.worldpay.offerservice.services.IProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OfferService implements IOfferService {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final IMerchantService merchantService;
    private final IProductService productService;

    @Autowired
    public OfferService(final OfferRepository offerRepository,
                        final OfferMapper offerMapper,
                        final MerchantService merchantService,
                        final IProductService productService) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
        this.merchantService = merchantService;
        this.productService = productService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OfferResponse insertOfferForMerchant(final String merchantId,
                                                final OfferRequest offerRequest)
        throws OfferServiceException {
        log.debug("Insert Merchant Offer with MerchantId: {} and OfferRequest: {}", merchantId,
                  offerRequest);
        final Merchant merchantByMerchantId = merchantService.findMerchantByMerchantId(merchantId);
        final Offer
            offerDb =
            offerMapper.convertOfferRequestToJpaEntity(offerRequest, merchantByMerchantId);
        final Offer savedOffer = offerRepository.save(offerDb);
        final List<ProductItemRequest> productItemRequests = offerRequest.getProductItemRequests();
        final List<ProductItemResponse>
            productItemResponses =
            productService.saveProductItemsForOffer(offerDb, productItemRequests);
        final MerchantResponse
            merchantResponseByMerchantEntity =
            merchantService.getMerchantResponseByMerchantEntity(
                merchantByMerchantId);
        return offerMapper.convertFullOfferToResponse(savedOffer, productItemResponses,
                                                      merchantResponseByMerchantEntity);
    }

    @Override
    public List<OfferResponse> getActiveOffersForMerchant(final String merchantId)
        throws OfferServiceException {
        final Merchant merchantByMerchantId = merchantService.findMerchantByMerchantId(merchantId);
        final List<Offer>
            offersByMerchantAnAndStatus =
            offerRepository.findOffersByMerchantMerchantIdAndStatus(
                merchantByMerchantId.getMerchantId(),
                OfferStatus.ACTIVE.toString());
        final List<OfferResponse> offerResponses = new ArrayList<>();
        offersByMerchantAnAndStatus.stream()
                                   .forEach(offer -> {
                                       final List<ProductItemResponse>
                                           allProductItemsResponsesForOffer =
                                           productService.getAllProductItemsResponsesForOffer(
                                               offer);
                                       final OfferResponse
                                           offerResponse =
                                           offerMapper.convertFullOfferToResponse(offer,
                                                                                  allProductItemsResponsesForOffer,
                                                                                  merchantService.getMerchantResponseByMerchantEntity(
                                                                                      merchantByMerchantId));
                                       offerResponses.add(offerResponse);
                                   });
        return offerResponses;
    }

    @Override
    public void cancelMerchantOffer(final String merchantId, final String offerId)
        throws OfferServiceException {
        final Merchant merchantByMerchantId = merchantService.findMerchantByMerchantId(merchantId);
        long providedOfferId = 0L;
        Offer offer = null;
        try {
            providedOfferId = Long.parseLong(offerId);
            final Optional<Offer>
                offerOptional =
                offerRepository.findById(providedOfferId);
            if (!offerOptional.isPresent()) {
                log.error("Offer with Id: {} for merchantId : {} was not found", offerId,
                          merchantId);
                throwOfferServiceException(ErrorMessagesEnum.OFFER_NOT_FOUND);
            }
            offer = offerOptional.get();
        } catch (final NumberFormatException e) {
            log.error("Random string {} is provided during cancel merchant offer", offerId);
            throwOfferServiceException(ErrorMessagesEnum.OFFER_NOT_FOUND);
        }
        if (!merchantByMerchantId.getMerchantId()
                                 .equals(offer.getMerchant()
                                              .getMerchantId())) {
            log.error("Wrong merchant with Id: {} trying to cancel offer with Id: {}", merchantId,
                      offerId);
            throwOfferServiceException(ErrorMessagesEnum.MERCHANT_DOES_NOT_OWN_OFFER);
        }
        offer.setStatus(OfferStatus.CANCELLED.toString());
        offer.setExpireDate(new Date());
        offerRepository.save(offer);
        log.debug("Offer with Id: {} was canceled");
    }

    private void throwOfferServiceException(final ErrorMessagesEnum errorMessagesEnum)
        throws OfferServiceException {
        throw new OfferServiceException(errorMessagesEnum.getMessage(),
                                        HttpStatus.BAD_REQUEST.value());
    }
}
