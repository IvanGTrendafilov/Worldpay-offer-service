package com.trendafilov.ivan.worldpay.offerservice.services;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.ProductItemRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.ProductItemResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Offer;

import java.util.List;

public interface IProductService {

    /**
     * Save product items into database for specific {@link Offer} and create list with response
     * objects
     *
     * @param offer               The offer for which we want to create {@link com.trendafilov.ivan.worldpay.offerservice.entities.ProductItem}
     * @param productItemRequests
     * @return List with {@link ProductItemResponse} objects
     */
    List<ProductItemResponse> saveProductItemsForOffer(Offer offer,
                                                       List<ProductItemRequest> productItemRequests);

    /**
     * Get all product items for {@link Offer}
     *
     * @param offer
     * @return List with {@link ProductItemResponse} objects
     */
    List<ProductItemResponse> getAllProductItemsResponsesForOffer(Offer offer);
}
