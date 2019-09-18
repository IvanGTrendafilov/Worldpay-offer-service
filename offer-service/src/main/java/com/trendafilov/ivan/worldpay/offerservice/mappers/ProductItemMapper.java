package com.trendafilov.ivan.worldpay.offerservice.mappers;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.ProductItemRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.response.ProductItemResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Offer;
import com.trendafilov.ivan.worldpay.offerservice.entities.ProductItem;

import org.springframework.stereotype.Component;

@Component
public class ProductItemMapper {

    public ProductItemResponse convertProductItemToResponse(final ProductItem productItem) {
        return ProductItemResponse.builder()
                                  .productDescription(productItem.getProductDescription())
                                  .productItemId(productItem.getProductItemId())
                                  .productType(productItem.getProductType())
                                  .build();
    }

    public ProductItem convertProductItemRequestToProductItemEntity(
        final ProductItemRequest productItemRequest, final Offer offer) {
        return ProductItem.builder()
                          .productDescription(
                              productItemRequest.getProductDescription())
                          .productType(
                              productItemRequest.getProductType())
                          .offer(offer)
                          .build();
    }
}
