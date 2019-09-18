package com.trendafilov.ivan.worldpay.offerservice.controllers;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.OfferRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.OfferResponse;
import com.trendafilov.ivan.worldpay.offerservice.exceptions.OfferServiceException;
import com.trendafilov.ivan.worldpay.offerservice.services.IOfferService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

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

    @ApiOperation(
        value = "Insert information for offer",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE,
        notes = "Insert merchant offer for specific product items. OfferServiceException is thrown when merchant is invalid",
        response = OfferResponse.class)
    @PostMapping(value = "merchants/{merchantId}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity insertOffer(@PathVariable final String merchantId,
                                      @Valid @RequestBody final OfferRequest offerRequest)
        throws OfferServiceException {
        final OfferResponse
            offerResponse =
            offerService.insertOfferForMerchant(merchantId, offerRequest);
        return new ResponseEntity<>(offerResponse, HttpStatus.CREATED);
    }

    @ApiOperation(
        value = "Gert all active offers for merchant",
        produces = MediaType.APPLICATION_JSON_VALUE,
        notes = "Gert all active offers for merchant. OfferServiceException is thrown when merchant is invalid",
        response = OfferResponse.class)
    @GetMapping(value = "merchants/{merchantId}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getActiveOffers(@PathVariable final String merchantId)
        throws OfferServiceException {
        final List<OfferResponse>
            activeOffersForMerchant =
            offerService.getActiveOffersForMerchant(merchantId);
        return new ResponseEntity<>(activeOffersForMerchant, HttpStatus.OK);
    }

    @ApiOperation(
        value = "Cancel Merchant offer",
        notes = "Cancel Merchant Offer. OfferServiceException is thrown when merchant/offer is invalid",
        response = ResponseEntity.class)
    @PutMapping(value = "merchants/{merchantId}/offers/{offerId}")
    public ResponseEntity cancelMerchantOffer(@PathVariable final String merchantId,
                                              @PathVariable final String offerId)
        throws OfferServiceException {
        offerService.cancelMerchantOffer(merchantId, offerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
