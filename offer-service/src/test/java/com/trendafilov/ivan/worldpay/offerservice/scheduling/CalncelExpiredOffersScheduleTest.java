package com.trendafilov.ivan.worldpay.offerservice.scheduling;

import com.trendafilov.ivan.worldpay.offerservice.entities.Merchant;
import com.trendafilov.ivan.worldpay.offerservice.entities.Offer;
import com.trendafilov.ivan.worldpay.offerservice.exceptions.OfferServiceException;
import com.trendafilov.ivan.worldpay.offerservice.repositories.OfferRepository;
import com.trendafilov.ivan.worldpay.offerservice.services.impl.OfferService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class CalncelExpiredOffersScheduleTest {

    @Mock
    private OfferRepository offerRepository;
    @Mock
    private OfferService offerService;

    @InjectMocks
    private CancelExpiredOffersSchedule cancelExpiredOffersSchedule;

    @Test
    public void test_cancelExpiredOffersSchedule() {
        final Offer mockOffer = mock(Offer.class);
        final List<Offer> offerList = Arrays.asList(mockOffer);
        when(offerRepository.findAllActiveWithExpireDateBefore(any())).thenReturn(offerList);
        final Merchant merchant = mock(Merchant.class);
        when(mockOffer.getMerchant()).thenReturn(merchant);
        final Long merchantId = new Random().nextLong();
        when(merchant.getMerchantId()).thenReturn(merchantId);
        final Long offerId = new Random().nextLong();
        when(mockOffer.getOfferId()).thenReturn(offerId);
        cancelExpiredOffersSchedule.cancelExpiredOffersSchedule();

        try {
            verify(offerService).cancelMerchantOffer(merchantId.toString(), offerId.toString());
        } catch (final OfferServiceException e) {
            fail();
        }
        verify(offerRepository).findAllActiveWithExpireDateBefore(any());
    }
}
