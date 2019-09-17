package com.trendafilov.ivan.worldpay.offerservice.services.impl;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.OfferRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.ProductItemRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.response.OfferResponse;
import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.response.ProductItemResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Merchant;
import com.trendafilov.ivan.worldpay.offerservice.entities.Offer;
import com.trendafilov.ivan.worldpay.offerservice.entities.ProductItem;
import com.trendafilov.ivan.worldpay.offerservice.enums.ErrorMessagesEnum;
import com.trendafilov.ivan.worldpay.offerservice.exceptions.OfferServiceException;
import com.trendafilov.ivan.worldpay.offerservice.mappers.MerchantMapper;
import com.trendafilov.ivan.worldpay.offerservice.mappers.OfferMapper;
import com.trendafilov.ivan.worldpay.offerservice.mappers.ProductItemMapper;
import com.trendafilov.ivan.worldpay.offerservice.repositories.MerchantRepository;
import com.trendafilov.ivan.worldpay.offerservice.repositories.OfferRepository;
import com.trendafilov.ivan.worldpay.offerservice.repositories.ProductItemRepository;
import com.trendafilov.ivan.worldpay.offerservice.services.IOfferService;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OfferService implements IOfferService {

    private final OfferRepository offerRepository;
    private final MerchantRepository merchantRepository;
    private final ProductItemRepository productItemRepository;
    private final MerchantMapper merchantMapper;
    private final ProductItemMapper productItemMapper;
    private final OfferMapper offerMapper;

    @Autowired
    public OfferService(final OfferRepository offerRepository,
                        final MerchantRepository merchantRepository,
                        final ProductItemRepository productItemRepository,
                        final MerchantMapper merchantMapper,
                        final ProductItemMapper productItemMapper,
                        final OfferMapper offerMapper) {
        this.offerRepository = offerRepository;
        this.merchantRepository = merchantRepository;
        this.productItemRepository = productItemRepository;
        this.merchantMapper = merchantMapper;
        this.productItemMapper = productItemMapper;
        this.offerMapper = offerMapper;
    }

    @Override
    public List<Offer> findAllOffersInStore() {
        return offerRepository.findAll();
    }

    @Override
    public OfferResponse insertOfferForMerchant(final String merchantId,
                                                final OfferRequest offerRequest)
        throws OfferServiceException {
        long providedMerchantId = 0L;
        Merchant merchant = null;
        try {
            providedMerchantId = Long.parseLong(merchantId);
            final Optional<Merchant>
                merchantOptional =
                merchantRepository.findById(providedMerchantId);
            if (!merchantOptional.isPresent()) {
                throw new OfferServiceException(ErrorMessagesEnum.MERCHANT_NOT_FOUND.getMessage(),
                                                HttpStatus.BAD_REQUEST.value());
            }
            merchant = merchantOptional.get();
        } catch (final NumberFormatException e) {
            throw new OfferServiceException(ErrorMessagesEnum.MERCHANT_NOT_FOUND.getMessage(),
                                            HttpStatus.BAD_REQUEST.value());
        }
        final Offer
            offerDb =
            Offer.builder()
                 .merchant(merchant)
                 .description(offerRequest.getDescription())
                 .price(offerRequest.getPrice())
                 .status(offerRequest.getStatus())
                 .currency(offerRequest.getCurrency())
                 .expireDate(DateUtils.addDays(new Date(), 10))
                 .build();
        final Offer savedOffer = offerRepository.save(offerDb);
        final List<ProductItemRequest> productItemRequests = offerRequest.getProductItemRequests();
        final List<ProductItemResponse> productItemResponses = new ArrayList<>();
        productItemRequests.stream()
                           .forEach(productItemRequest -> {
                               ProductItem dbProductItem = ProductItem.builder()
                                                                      .productDescription(
                                                                          productItemRequest.getProductDescription())
                                                                      .productType(
                                                                          productItemRequest.getProductType())
                                                                      .offer(savedOffer)
                                                                      .build();
                               dbProductItem = productItemRepository.save(dbProductItem);
                               productItemResponses.add(
                                   productItemMapper.convertProductItemToResponse(dbProductItem));
                           });
        return offerMapper.convertFullOfferToResponse(savedOffer, productItemResponses,
                                                      merchantMapper.convertMerchantToResponse(
                                                          merchant));
    }
}
