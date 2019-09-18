package com.trendafilov.ivan.worldpay.offerservice.services.impl;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.OfferRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.ProductItemRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.OfferResponse;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.ProductItemResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Merchant;
import com.trendafilov.ivan.worldpay.offerservice.entities.Offer;
import com.trendafilov.ivan.worldpay.offerservice.enums.OfferStatus;
import com.trendafilov.ivan.worldpay.offerservice.exceptions.OfferServiceException;
import com.trendafilov.ivan.worldpay.offerservice.mappers.MerchantMapper;
import com.trendafilov.ivan.worldpay.offerservice.mappers.OfferMapper;
import com.trendafilov.ivan.worldpay.offerservice.repositories.OfferRepository;
import com.trendafilov.ivan.worldpay.offerservice.services.IMerchantService;
import com.trendafilov.ivan.worldpay.offerservice.services.IOfferService;
import com.trendafilov.ivan.worldpay.offerservice.services.IProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OfferService implements IOfferService {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final IMerchantService merchantService;
    private final IProductService productService;

    @Autowired
    public OfferService(final OfferRepository offerRepository,
                        final MerchantMapper merchantMapper,
                        final OfferMapper offerMapper,
                        final MerchantService merchantService,
                        final IProductService productService) {
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
        this.merchantService = merchantService;
        this.productService = productService;
    }

    @Override
    public List<Offer> findAllOffersInStore() {
        return offerRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OfferResponse insertOfferForMerchant(final String merchantId,
                                                final OfferRequest offerRequest)
        throws OfferServiceException {
        final Merchant merchantByMerchantId = merchantService.findMerchantByMerchantId(merchantId);
        final Offer
            offerDb =
            offerMapper.convertOfferRequestToJpaEntity(offerRequest, merchantByMerchantId);
        final Offer savedOffer = offerRepository.save(offerDb);
        final List<ProductItemRequest> productItemRequests = offerRequest.getProductItemRequests();
        final List<ProductItemResponse>
            productItemResponses =
            productService.saveProductItemsForOffer(offerDb, productItemRequests);
        return offerMapper.convertFullOfferToResponse(savedOffer, productItemResponses,
                                                      merchantService.getMerchantResponseByMerchantEntity(
                                                          merchantByMerchantId));
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
}
