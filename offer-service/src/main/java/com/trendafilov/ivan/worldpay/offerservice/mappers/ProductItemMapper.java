package com.trendafilov.ivan.worldpay.offerservice.mappers;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.response.ProductItemResponse;
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
}
