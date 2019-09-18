package com.trendafilov.ivan.worldpay.offerservice.services;

import com.trendafilov.ivan.worldpay.offerservice.dtos.response.MerchantResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Merchant;
import com.trendafilov.ivan.worldpay.offerservice.enums.ErrorMessagesEnum;
import com.trendafilov.ivan.worldpay.offerservice.exceptions.OfferServiceException;
import com.trendafilov.ivan.worldpay.offerservice.mappers.MerchantMapper;
import com.trendafilov.ivan.worldpay.offerservice.repositories.MerchantRepository;
import com.trendafilov.ivan.worldpay.offerservice.services.impl.MerchantService;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
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
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
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
        final Long merchantId = new Random().nextLong();
        final Optional<Merchant> merchantMock = Optional.of(mock(
            Merchant.class));
        when(merchantRepository.findById(anyLong()))
            .thenReturn(merchantMock);
        final Merchant
            merchantByMerchantId =
            merchantService.findMerchantByMerchantId(merchantId.toString());
        assertNotNull(merchantByMerchantId);
        assertEquals(merchantMock.get(), merchantByMerchantId);
        verify(merchantRepository, times(1)).findById(anyLong());
    }

    @Test
    public void test_findMerchantByMerchantIdWithInvalidMerchantId() {
        final String merchantId = RandomStringUtils.random(5);
        try {
            merchantService.findMerchantByMerchantId(merchantId);
        } catch (final OfferServiceException e) {
            assertEquals(e.getMessage(), ErrorMessagesEnum.MERCHANT_NOT_FOUND.getMessage());
            assertTrue(e.getStatusCode() == HttpStatus.BAD_REQUEST.value());
        }
    }

    @Test
    public void test_findMerchantByMerchantIdWithNotFoundRecord() {

        final Long merchantId = new Random().nextLong();
        final Optional<Merchant> merchantMock = Optional.ofNullable(null);
        when(merchantRepository.findById(anyLong()))
            .thenReturn(merchantMock);
        try {
            merchantService.findMerchantByMerchantId(merchantId.toString());
        } catch (final OfferServiceException e) {
            assertEquals(e.getMessage(), ErrorMessagesEnum.MERCHANT_NOT_FOUND.getMessage());
            assertTrue(e.getStatusCode() == HttpStatus.BAD_REQUEST.value());
        }
    }

    @Test
    public void test_getAllMerchants() {
        final Merchant mock = mock(Merchant.class);
        final MerchantResponse merchantResponse = mock(MerchantResponse.class);
        when(merchantRepository.findAll()).thenReturn(Arrays.asList(mock));
        doReturn(merchantResponse).when(spy)
                                  .getMerchantResponseByMerchantEntity(mock);
        final List<MerchantResponse> allMerchants = spy.getAllMerchants();
        assertFalse(allMerchants.isEmpty());
        assertTrue(allMerchants.size() == 1);
        assertEquals(allMerchants.get(0), merchantResponse);
    }
}
