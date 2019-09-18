package com.trendafilov.ivan.worldpay.offerservice.services.impl;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.ProductItemRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.response.ProductItemResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Offer;
import com.trendafilov.ivan.worldpay.offerservice.entities.ProductItem;
import com.trendafilov.ivan.worldpay.offerservice.mappers.ProductItemMapper;
import com.trendafilov.ivan.worldpay.offerservice.repositories.ProductItemRepository;
import com.trendafilov.ivan.worldpay.offerservice.services.IProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService implements IProductService {

    private final ProductItemRepository productItemRepository;
    private final ProductItemMapper productItemMapper;

    @Autowired
    public ProductService(
        final ProductItemRepository productItemRepository,
        final ProductItemMapper productItemMapper) {
        this.productItemRepository = productItemRepository;
        this.productItemMapper = productItemMapper;
    }

    @Override
    public List<ProductItemResponse> saveProductItemsForOffer(final Offer offer,
                                                              final List<ProductItemRequest> productItemRequests) {
        final List<ProductItemResponse> productItemResponses = new ArrayList<>();
        productItemRequests.stream()
                           .forEach(productItemRequest -> {
                               ProductItem dbProductItem = ProductItem.builder()
                                                                      .productDescription(
                                                                          productItemRequest.getProductDescription())
                                                                      .productType(
                                                                          productItemRequest.getProductType())
                                                                      .offer(offer)
                                                                      .build();
                               dbProductItem = productItemRepository.save(dbProductItem);
                               productItemResponses.add(
                                   productItemMapper.convertProductItemToResponse(dbProductItem));
                           });
        return productItemResponses;
    }
}
