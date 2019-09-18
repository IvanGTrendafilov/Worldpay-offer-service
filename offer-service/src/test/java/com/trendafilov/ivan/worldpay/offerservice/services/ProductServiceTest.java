package com.trendafilov.ivan.worldpay.offerservice.services;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.ProductItemRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.ProductItemResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Offer;
import com.trendafilov.ivan.worldpay.offerservice.entities.ProductItem;
import com.trendafilov.ivan.worldpay.offerservice.mappers.ProductItemMapper;
import com.trendafilov.ivan.worldpay.offerservice.repositories.ProductItemRepository;
import com.trendafilov.ivan.worldpay.offerservice.services.impl.ProductService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProductServiceTest {

    @Mock
    private ProductItemRepository productItemRepository;
    @Mock
    private ProductItemMapper productItemMapper;
    @InjectMocks
    private ProductService productService;

    @Test
    public void test_saveProductItemsForOffer() {
        final Offer mockOffer = mock(Offer.class);
        final ProductItemRequest productItemRequest = mock(ProductItemRequest.class);
        final ProductItemResponse productItemResponse = mock(ProductItemResponse.class);
        final List<ProductItemRequest>
            productItemRequests =
            Arrays.asList(productItemRequest);
        final ProductItem productItem = mock(ProductItem.class);
        when(productItemMapper.convertProductItemRequestToProductItemEntity(
            productItemRequest, mockOffer)).thenReturn(productItem);
        when(productItemRepository.save(productItem)).thenReturn(productItem);
        when(productItemMapper.convertProductItemToResponse(productItem)).thenReturn(
            productItemResponse);

        final List<ProductItemResponse>
            productItemResponses =
            productService.saveProductItemsForOffer(mockOffer, productItemRequests);

        assertFalse(productItemResponses.isEmpty());
        assertTrue(productItemResponses.contains(productItemResponse));
        verify(productItemMapper).convertProductItemRequestToProductItemEntity(
            eq(productItemRequest), eq(mockOffer));
        verify(productItemRepository).save(productItem);
        verify(productItemMapper).convertProductItemToResponse(productItem);

    }

    @Test
    public void test_getAllProductItemsResponsesForOffer() {
        final Offer mockOffer = mock(Offer.class);
        final ProductItem productItem = mock(ProductItem.class);
        final ProductItemResponse productItemResponse = mock(ProductItemResponse.class);
        when(mockOffer.getProductItems()).thenReturn(Arrays.asList(productItem));
        when(productItemMapper.convertProductItemToResponse(
            productItem)).thenReturn(productItemResponse);

        final List<ProductItemResponse>
            allProductItemsResponsesForOffer =
            productService.getAllProductItemsResponsesForOffer(mockOffer);

        assertFalse(allProductItemsResponsesForOffer.isEmpty());
        assertTrue(allProductItemsResponsesForOffer.contains(productItemResponse));
        verify(productItemMapper).convertProductItemToResponse(eq(productItem));
    }
}
