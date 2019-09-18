package com.trendafilov.ivan.worldpay.offerservice.services;

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
import com.trendafilov.ivan.worldpay.offerservice.services.impl.MerchantService;
import com.trendafilov.ivan.worldpay.offerservice.services.impl.OfferService;
import com.trendafilov.ivan.worldpay.offerservice.services.impl.ProductService;

import junit.framework.TestCase;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class OfferServiceTest {

    @Mock
    private OfferRepository offerRepository;
    @Mock
    private OfferMapper offerMapper;
    @Mock
    private MerchantService merchantService;
    @Mock
    private ProductService productService;
    @InjectMocks
    private OfferService offerService;

    @Test
    public void test_insertOfferForInvalidMerchant() throws OfferServiceException {
        // Given
        final String merchantId = RandomStringUtils.random(5);
        final Merchant mockMerchant = mock(Merchant.class);
        final MerchantResponse merchantResponse = mock(MerchantResponse.class);
        final OfferRequest offerRequest = mock(OfferRequest.class);
        final Offer mockOffer = mock(Offer.class);
        when(merchantService.findMerchantByMerchantId(merchantId)).thenReturn(mockMerchant);
        when(offerMapper.convertOfferRequestToJpaEntity(offerRequest, mockMerchant)).thenReturn(
            mockOffer);
        when(offerRepository.save(mockOffer)).thenReturn(mockOffer);
        final ArrayList<ProductItemRequest> productItemRequests = new ArrayList<>();
        when(offerRequest.getProductItemRequests()).thenReturn(productItemRequests);
        final ArrayList<ProductItemResponse> productItemResponses = new ArrayList<>();
        when(productService.saveProductItemsForOffer(mockOffer, productItemRequests)).thenReturn(
            productItemResponses);
        when(merchantService.getMerchantResponseByMerchantEntity(
            mockMerchant)).thenReturn(merchantResponse);
        final OfferResponse mock = mock(
            OfferResponse.class);
        when(offerMapper.convertFullOfferToResponse(mockOffer, productItemResponses,
                                                    merchantResponse)).thenReturn(mock);
        // When
        final OfferResponse
            offerResponse =
            offerService.insertOfferForMerchant(merchantId, offerRequest);
        // Then
        assertNotNull(offerResponse);
        assertEquals(offerResponse, mock);
        verify(merchantService).findMerchantByMerchantId(eq(merchantId));
        verify(offerMapper).convertOfferRequestToJpaEntity(eq(offerRequest), eq(mockMerchant));
        verify(offerRepository).save(eq(mockOffer));
        verify(productService).saveProductItemsForOffer(eq(mockOffer), eq(productItemRequests));
        verify(merchantService).getMerchantResponseByMerchantEntity(
            eq(mockMerchant));
        verify(offerMapper).convertFullOfferToResponse(eq(mockOffer), eq(productItemResponses),
                                                       eq(merchantResponse));
    }

    @Test
    public void test_getActiveOffersForMerchantSuccess() throws OfferServiceException {
        // Given
        final Long merchantId = new Random().nextLong();
        final OfferResponse offerResponse = mock(
            OfferResponse.class);
        final Merchant mockMerchant = mock(Merchant.class);
        final Offer mockOffer = mock(Offer.class);
        final MerchantResponse merchantResponse = mock(MerchantResponse.class);
        final List<ProductItemResponse>
            productItemResponses =
            Arrays.asList(mock(ProductItemResponse.class));
        when(merchantService.findMerchantByMerchantId(merchantId.toString())).thenReturn(
            mockMerchant);
        final List<Offer> offerList = Arrays.asList(mockOffer);
        when(mockMerchant.getMerchantId()).thenReturn(merchantId);
        when(offerRepository.findOffersByMerchantMerchantIdAndStatus(
            merchantId,
            OfferStatus.ACTIVE.toString())).thenReturn(offerList);
        when(productService.getAllProductItemsResponsesForOffer(
            mockOffer)).thenReturn(productItemResponses);
        when(merchantService.getMerchantResponseByMerchantEntity(
            mockMerchant)).thenReturn(merchantResponse);
        when(offerMapper.convertFullOfferToResponse(mockOffer,
                                                    productItemResponses,
                                                    merchantResponse)).thenReturn(offerResponse);
        // When
        final List<OfferResponse>
            activeOffersForMerchant =
            offerService.getActiveOffersForMerchant(merchantId.toString());
        // Then
        assertFalse(activeOffersForMerchant.isEmpty());
        assertTrue(activeOffersForMerchant.contains(offerResponse));
        verify(merchantService).findMerchantByMerchantId(eq(merchantId.toString()));
        verify(offerRepository).findOffersByMerchantMerchantIdAndStatus(
            merchantId,
            OfferStatus.ACTIVE.toString());
        verify(productService).getAllProductItemsResponsesForOffer(eq(mockOffer));
        verify(merchantService).getMerchantResponseByMerchantEntity(
            eq(mockMerchant));
        verify(offerMapper).convertFullOfferToResponse(eq(mockOffer), eq(productItemResponses),
                                                       eq(merchantResponse));
    }

    @Test
    public void test_cancelMerchantOfferSuccess() throws OfferServiceException {
        // Given
        final Long merchantId = new Random().nextLong();
        final Long offerId = new Random().nextLong();
        final Merchant merchantMock = mock(Merchant.class);
        final Optional<Offer> offerMock = Optional.of(mock(
            Offer.class));
        when(merchantService.findMerchantByMerchantId(merchantId.toString()))
            .thenReturn(merchantMock);
        when(offerRepository.findById(anyLong())).thenReturn(offerMock);
        // When
        offerService.cancelMerchantOffer(merchantId.toString(), offerId.toString());
        // Then
        verify(offerRepository, times(1)).findById(anyLong());
        verify(offerRepository, times(1)).save(offerMock.get());
    }

    @Test
    public void test_cancelMerchantOfferWithInvalidMerchantId() throws OfferServiceException {
        // Given
        final String merchantId = RandomStringUtils.random(5);
        final Long offerId = new Random().nextLong();
        final Merchant merchantMock = mock(Merchant.class);
        when(merchantService.findMerchantByMerchantId(merchantId))
            .thenReturn(merchantMock);
        try {
            // When
            offerService.cancelMerchantOffer(merchantId, offerId.toString());
        } catch (final OfferServiceException e) {
            // Then
            assertEquals(e.getMessage(), ErrorMessagesEnum.OFFER_NOT_FOUND.getMessage());
            TestCase.assertTrue(e.getStatusCode() == HttpStatus.BAD_REQUEST.value());
        }
    }

    @Test
    public void test_cancelMerchantOfferWhenOfferNotFound() throws OfferServiceException {
        // Given
        final String merchantId = RandomStringUtils.random(5);
        final Long offerId = new Random().nextLong();
        final Merchant merchantMock = mock(Merchant.class);
        when(merchantService.findMerchantByMerchantId(merchantId))
            .thenReturn(merchantMock);
        final Optional<Offer> offerOptional = Optional.ofNullable(null);
        when(offerRepository.findById(anyLong())).thenReturn(offerOptional);
        try {
            // When
            offerService.cancelMerchantOffer(merchantId, offerId.toString());
        } catch (final OfferServiceException e) {
            // Then
            assertEquals(e.getMessage(), ErrorMessagesEnum.OFFER_NOT_FOUND.getMessage());
            TestCase.assertTrue(e.getStatusCode() == HttpStatus.BAD_REQUEST.value());
        }
    }
}
