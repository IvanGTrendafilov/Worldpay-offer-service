package com.trendafilov.ivan.worldpay.offerservice.services.impl;

import com.trendafilov.ivan.worldpay.offerservice.entities.Offer;
import com.trendafilov.ivan.worldpay.offerservice.repositories.OfferRepository;
import com.trendafilov.ivan.worldpay.offerservice.services.IOfferService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferService implements IOfferService {

    private final OfferRepository offerRepository;

    @Autowired
    public OfferService(final OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    @Override
    public List<Offer> findAllOffersInStore() {
        return offerRepository.findAll();
    }
}
