package com.trendafilov.ivan.worldpay.offerservice.services;

import com.trendafilov.ivan.worldpay.offerservice.entities.Offer;

import java.util.List;

public interface IOfferService {

    List<Offer> findAllOffersInStore();
}
