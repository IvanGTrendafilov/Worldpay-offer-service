package com.trendafilov.ivan.worldpay.offerservice.scheduling;

import com.trendafilov.ivan.worldpay.offerservice.entities.Offer;
import com.trendafilov.ivan.worldpay.offerservice.exceptions.OfferServiceException;
import com.trendafilov.ivan.worldpay.offerservice.repositories.OfferRepository;
import com.trendafilov.ivan.worldpay.offerservice.services.impl.OfferService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class CancelExpiredOffersSchedule {

    private final OfferRepository offerRepository;
    private final OfferService offerService;

    @Autowired
    public CancelExpiredOffersSchedule(
        final OfferRepository offerRepository,
        final OfferService offerService) {
        this.offerRepository = offerRepository;
        this.offerService = offerService;
    }

    @Scheduled(fixedRate = 60000)
    public void cancelExpiredOffersSchedule() {
        final List<Offer>
            allWithExpireDateBefore =
            offerRepository.findAllActiveWithExpireDateBefore(new Date());
        allWithExpireDateBefore.stream()
                               .forEach(
                                   offer -> {
                                       try {
                                           offerService.cancelMerchantOffer(offer.getMerchant()
                                                                                 .getMerchantId()
                                                                                 .toString(),
                                                                            offer.getOfferId()
                                                                                 .toString());
                                       } catch (final OfferServiceException e) {
                                           // Logger must be added here
                                           e.printStackTrace();
                                       }
                                   });
    }
}
