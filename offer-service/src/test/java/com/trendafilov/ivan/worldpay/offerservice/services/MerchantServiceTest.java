package com.trendafilov.ivan.worldpay.offerservice.services;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.MerchantRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.MerchantResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Merchant;
import com.trendafilov.ivan.worldpay.offerservice.enums.ErrorMessagesEnum;
import com.trendafilov.ivan.worldpay.offerservice.exceptions.OfferServiceException;
import com.trendafilov.ivan.worldpay.offerservice.mappers.MerchantMapper;
import com.trendafilov.ivan.worldpay.offerservice.repositories.MerchantRepository;
import com.trendafilov.ivan.worldpay.offerservice.services.impl.MerchantService;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@Ignore
public class MerchantServiceTest {

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private MerchantMapper merchantMapper;

    @InjectMocks
    private MerchantService merchantService;

    private MerchantService spy;

    @Before
    public void setup() {
        spy = spy(merchantService);
    }

    @Test
    public void test_findMerchantByMerchantIdSuccess() throws OfferServiceException {
        // Given
        final Long merchantId = new Random().nextLong();
        final Optional<Merchant> merchantMock = Optional.of(mock(
            Merchant.class));
        when(merchantRepository.findById(anyLong()))
            .thenReturn(merchantMock);
        // When
        final Merchant
            merchantByMerchantId =
            merchantService.findMerchantByMerchantId(merchantId.toString());
        // Then
        assertNotNull(merchantByMerchantId);
        assertEquals(merchantMock.get(), merchantByMerchantId);
        verify(merchantRepository, times(1)).findById(anyLong());
    }

    @Test
    public void test_findMerchantByMerchantIdWithInvalidMerchantId() {
        // Given
        final String merchantId = RandomStringUtils.random(5);
        try {
            // When
            merchantService.findMerchantByMerchantId(merchantId);
        } catch (final OfferServiceException e) {
            // Then
            assertEquals(e.getMessage(), ErrorMessagesEnum.MERCHANT_NOT_FOUND.getMessage());
            assertTrue(e.getStatusCode() == HttpStatus.BAD_REQUEST.value());
        }
    }

    @Test
    public void test_findMerchantByMerchantIdWithNotFoundRecord() {

        // Given
        final Long merchantId = new Random().nextLong();
        final Optional<Merchant> merchantMock = Optional.ofNullable(null);
        when(merchantRepository.findById(anyLong()))
            .thenReturn(merchantMock);
        try {
            // When
            merchantService.findMerchantByMerchantId(merchantId.toString());
        } catch (final OfferServiceException e) {
            // Then
            assertEquals(e.getMessage(), ErrorMessagesEnum.MERCHANT_NOT_FOUND.getMessage());
            assertTrue(e.getStatusCode() == HttpStatus.BAD_REQUEST.value());
        }
    }

    @Test
    public void test_getAllMerchants() {
        // Given
        final Merchant mock = mock(Merchant.class);
        final MerchantResponse merchantResponse = mock(MerchantResponse.class);
        when(merchantRepository.findAll()).thenReturn(Arrays.asList(mock));
        doReturn(merchantResponse).when(spy)
                                  .getMerchantResponseByMerchantEntity(mock);
        // When
        final List<MerchantResponse> allMerchants = spy.getAllMerchants();
        // Then
        assertFalse(allMerchants.isEmpty());
        assertTrue(allMerchants.size() == 1);
        assertEquals(allMerchants.get(0), merchantResponse);
    }

    @Test
    public void test_getMerchantResponseByMerchantEntity() {
// Given
        final Merchant merchant = mock(Merchant.class);
        final MerchantResponse merchantResponse = mock(MerchantResponse.class);
        when(merchantMapper.convertMerchantToResponse(
            merchant)).thenReturn(merchantResponse);
        // When
        final MerchantResponse
            merchantResponseByMerchantEntity =
            merchantService.getMerchantResponseByMerchantEntity(merchant);
        // Then
        verify(merchantMapper).convertMerchantToResponse(eq(merchant));
        assertEquals(merchantResponseByMerchantEntity, merchantResponse);
    }

    @Test
    public void test_createMerchantByMerchantRequestWithEmptyFirstName() {
        // Given
        final MerchantRequest merchantRequest = mock(MerchantRequest.class);
        // When
        try {
            merchantService.createMerchantByMerchantRequest(merchantRequest);
        } catch (final OfferServiceException e) {
            // Then
            assertEquals(e.getMessage(), ErrorMessagesEnum.MERCHANT_NAME_EMPTY.getMessage());
            assertTrue(e.getStatusCode() == HttpStatus.BAD_REQUEST.value());
        }
    }

    @Test
    public void test_createMerchantByMerchantRequestWithEmptyDepartment() {
        // Given
        final MerchantRequest merchantRequest = mock(MerchantRequest.class);
        when(merchantRequest.getFirstName()).thenReturn(RandomStringUtils.random(5));
        // When
        try {
            merchantService.createMerchantByMerchantRequest(merchantRequest);
        } catch (final OfferServiceException e) {
            // Then
            assertEquals(e.getMessage(), ErrorMessagesEnum.MERCHANT_DEPARTMENT_EMPTY.getMessage());
            assertTrue(e.getStatusCode() == HttpStatus.BAD_REQUEST.value());
        }
    }

    @Test
    public void test_createMerchantByMerchantRequestSuccess() throws OfferServiceException {
        // Given
        final MerchantRequest merchantRequest = mock(MerchantRequest.class);
        when(merchantRequest.getFirstName()).thenReturn(RandomStringUtils.random(5));
        when(merchantRequest.getDepartment()).thenReturn(RandomStringUtils.random(5));
        final Merchant mock = mock(Merchant.class);
        when(merchantMapper.convertMerchantRequestToEntity(merchantRequest)).thenReturn(
            mock);
        when(merchantRepository.save(mock)).thenReturn(mock);
        final MerchantResponse merchantResponse = mock(MerchantResponse.class);
        when(merchantMapper.convertMerchantToResponse(
            mock)).thenReturn(merchantResponse);
        // When
        final MerchantResponse
            merchantByMerchantRequest =
            merchantService.createMerchantByMerchantRequest(merchantRequest);
        // Then
        assertNotNull(merchantByMerchantRequest);
        assertEquals(merchantByMerchantRequest, merchantResponse);
    }

}
