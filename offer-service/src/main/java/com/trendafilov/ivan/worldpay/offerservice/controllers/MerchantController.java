package com.trendafilov.ivan.worldpay.offerservice.controllers;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.MerchantRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.MerchantResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Merchant;
import com.trendafilov.ivan.worldpay.offerservice.mappers.MerchantMapper;
import com.trendafilov.ivan.worldpay.offerservice.services.IMerchantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(value = "Merchant Controller")
@RestController
@RequestMapping("/merchant/v1")
public class MerchantController {

    private final IMerchantService merchantService;
    private final MerchantMapper merchantMapper;

    @Autowired
    public MerchantController(
        final IMerchantService merchantService,
        final MerchantMapper merchantMapper) {
        this.merchantService = merchantService;
        this.merchantMapper = merchantMapper;
    }

    @ApiOperation(
        value = "Insert merchant",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE,
        notes = "Insert merchant",
        response = MerchantResponse.class)
    @PostMapping(
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveMerchant(
        @Valid @RequestBody final MerchantRequest merchantRequest) {
        log.info("Inside save merchant endpoint with Request body: {}", merchantRequest);
        final MerchantResponse
            merchantResponse =
            merchantService.createMerchantByMerchantRequest(merchantRequest);
        log.info("Save merchant endpoint return Response body: {}", merchantResponse);
        return new ResponseEntity<>(merchantResponse, HttpStatus.CREATED);
    }

    @ApiOperation(
        value = "Get all merchants",
        produces = MediaType.APPLICATION_JSON_VALUE,
        notes = "Get all Merchants",
        response = MerchantResponse.class)
    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllMerchants() {
        log.info("Inside get All merchants endpoint");
        final List<MerchantResponse> allMerchants = merchantService.getAllMerchants();
        return new ResponseEntity<>(allMerchants, HttpStatus.OK);
    }

    @ApiOperation(
        value = "Get merchant by Id ",
        produces = MediaType.APPLICATION_JSON_VALUE,
        notes = "Get merchant by id. OfferServiceException is thrown when merchant is invalid",
        response = MerchantResponse.class)
    @GetMapping(value = "/{merchantId}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getMerchantById(@PathVariable final String merchantId) {
        log.info("Inside get merchant by id endpoint with merchant id: {}", merchantId);
        final Merchant merchant = merchantService.findMerchantByMerchantId(merchantId);
        return new ResponseEntity<>(merchantMapper.convertMerchantToResponse(merchant),
                                    HttpStatus.OK);
    }
}
