package com.trendafilov.ivan.worldpay.offerservice.controllers;

import com.trendafilov.ivan.worldpay.offerservice.entities.Offer;
import com.trendafilov.ivan.worldpay.offerservice.services.IOfferService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Offer")
@RestController
@RequestMapping("/offer/v1")
public class OfferController {

    private final IOfferService offerService;

    @Autowired
    public OfferController(
        final IOfferService offerService) {
        this.offerService = offerService;
    }

    @ApiOperation(value = "Get all offers in store",
        produces = MediaType.APPLICATION_JSON_VALUE,
        notes = "Search all offers in store",
        response = Offer.class)
    @GetMapping
    public ResponseEntity getAllOffersInStore() {
        return ResponseEntity.ok(offerService.findAllOffersInStore());
    }
}
