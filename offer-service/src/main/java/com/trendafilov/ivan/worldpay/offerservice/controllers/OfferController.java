package com.trendafilov.ivan.worldpay.offerservice.controllers;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.OfferRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.OfferResponse;
import com.trendafilov.ivan.worldpay.offerservice.enums.OfferStatus;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value = "Offer")
@RestController
@RequestMapping("/offer/v1")
@Slf4j
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
                                      @Valid @RequestBody final OfferRequest offerRequest) {
        log.info("Insert offer endpoint with merchant id: {} and OfferRequest Request body: {}",
                 merchantId, offerRequest);
        final OfferResponse
            offerResponse =
            offerService.insertOfferForMerchant(merchantId, offerRequest);
        return new ResponseEntity<>(offerResponse, HttpStatus.CREATED);
    }

    @ApiOperation(
        value = "Get all offers for merchant by specific status",
        produces = MediaType.APPLICATION_JSON_VALUE,
        notes = "Gert all offers for merchant for specific offer status. OfferServiceException is thrown when merchant is invalid",
        response = OfferResponse.class)
    @GetMapping(value = "merchants/{merchantId}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getOffersForMerchantByStatus(
        @RequestHeader(name = "offer-status") final String offerStatus,
        @PathVariable final String merchantId) {
        log.info("Get all Offers for merchant with Id: {}", merchantId);
        final List<OfferResponse>
            activeOffersForMerchant =
            offerService.getOfferByMerchantAndStatus(merchantId, offerStatus);
        return new ResponseEntity<>(activeOffersForMerchant, HttpStatus.OK);
    }

    @ApiOperation(
        value = "Get all offers to Student.",
        produces = MediaType.APPLICATION_JSON_VALUE,
        notes = "Gert all offers for Student. OfferServiceException is thrown when Student is invalid",
        response = OfferResponse.class)
    @GetMapping(value = "students/{studentId}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllOffersToStudent(
        @PathVariable final String studentId) {
        log.info("Get all Offers for student with Id: {}", studentId);
        final List<OfferResponse>
            allOffersToStudent =
            offerService.getAllOffersToStudent(studentId);
        return new ResponseEntity<>(allOffersToStudent, HttpStatus.OK);
    }

    @ApiOperation(
        value = "Cancel Merchant offer",
        notes = "Cancel Merchant Offer. OfferServiceException is thrown when merchant/offer is invalid",
        response = ResponseEntity.class)
    @PutMapping(value = "merchants/{merchantId}/offers/{offerId}")
    public ResponseEntity cancelMerchantOffer(@PathVariable final String merchantId,
                                              @PathVariable final String offerId) {
        log.info("Cancel merchant offer with merchant id: {} and Offer Id: {}", merchantId,
                 offerId);
        offerService.cancelMerchantOffer(merchantId, offerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(
        value = "Assign Student to offer",
        notes = "Assign Student to Offer. OfferServiceException is thrown when student/offer is invalid of Student has accepted Offer",
        response = ResponseEntity.class)
    @PutMapping(value = "merchants/{merchantId}/students/{studentId}/offers/{offerId}")
    public ResponseEntity assignStudentToOffer(@PathVariable final String merchantId,
                                               @PathVariable final String studentId,
                                               @PathVariable final String offerId) {
        log.info("Student assign to offer with student id: {} and Offer Id: {}", studentId,
                 offerId);
        final OfferResponse
            offerResponse =
            offerService.assignStudentToOffer(studentId, offerId, merchantId);
        return new ResponseEntity<>(offerResponse, HttpStatus.OK);
    }

    @ApiOperation(
        value = "Accept Student offer",
        notes = "Accept Student Offer. OfferServiceException is thrown when student/offer is invalid",
        response = ResponseEntity.class)
    @PutMapping(value = "students/{studentId}/offers/{offerId}/accept")
    public ResponseEntity acceptStudentOffer(@PathVariable final String studentId,
                                             @PathVariable final String offerId) {
        log.info("Student accept offer with student id: {} and Offer Id: {}", studentId,
                 offerId);
        offerService.changeOfferStatusForStudent(studentId, offerId, OfferStatus.ACCEPTED);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(
        value = "Decline Student offer",
        notes = "Decline Student Offer. OfferServiceException is thrown when student/offer is invalid",
        response = ResponseEntity.class)
    @PutMapping(value = "students/{studentId}/offers/{offerId}/decline")
    public ResponseEntity declineStudentOffer(@PathVariable final String studentId,
                                              @PathVariable final String offerId) {
        log.info("Student accept offer offer with student id: {} and Offer Id: {}", studentId,
                 offerId);
        offerService.changeOfferStatusForStudent(studentId, offerId, OfferStatus.DECLINED);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
