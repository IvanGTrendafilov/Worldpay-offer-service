package com.trendafilov.ivan.worldpay.offergateway;

import com.trendafilov.ivan.worldpay.offergateway.dtos.Offer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "offer-service")
public interface OfferClient {

    @GetMapping("/offer/v1")
    List<Offer> findAllOffersInStore();
}
