package com.trendafilov.ivan.worldpay.offerservice.services;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.ProductItemRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.response.ProductItemResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Offer;

import java.util.List;

public interface IProductService {

    List<ProductItemResponse> saveProductItemsForOffer(Offer offer,
                                                       List<ProductItemRequest> productItemRequests);

    List<ProductItemResponse> getAllProductItemsResponsesForOffer(Offer offer);
}
