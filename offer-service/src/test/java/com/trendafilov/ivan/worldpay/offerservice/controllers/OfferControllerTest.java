package com.trendafilov.ivan.worldpay.offerservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendafilov.ivan.worldpay.offerservice.TestConstants;
import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.OfferRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.ProductItemRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.OfferResponse;
import com.trendafilov.ivan.worldpay.offerservice.enums.ErrorMessagesEnum;
import com.trendafilov.ivan.worldpay.offerservice.enums.OfferStatus;
import com.trendafilov.ivan.worldpay.offerservice.exceptions.OfferServiceException;
import com.trendafilov.ivan.worldpay.offerservice.services.impl.OfferService;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Ignore
public class OfferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OfferService offerService;

    @Test
    public void test_cancelMerchantOffer() throws Exception {
        final Long merchantId = new Random().nextLong();
        final Long offerId = new Random().nextLong();

        mockMvc.perform(
            put(TestConstants.OFFER_CONTROLLER_URI + "/" + "merchants/" + merchantId
                + "/offers/" + offerId).headers(getHttpHeaders()))
               .andExpect(status().isNoContent())
               .andDo(print());
    }

    @Test
    public void test_cancelMerchantOfferWithInvalidMerchant() throws Exception {
        final Long merchantId = new Random().nextLong();
        final Long offerId = new Random().nextLong();
        doThrow(new OfferServiceException(ErrorMessagesEnum.MERCHANT_NOT_FOUND.getMessage(),
                                          HttpStatus.BAD_REQUEST.value())).when(offerService)
                                                                          .cancelMerchantOffer(
                                                                              merchantId.toString(),
                                                                              offerId.toString());
        mockMvc.perform(
            put(TestConstants.OFFER_CONTROLLER_URI + "/" + "merchants/" + merchantId
                + "/offers/" + offerId).headers(getHttpHeaders()))
               .andExpect(status().isBadRequest())
               .andExpect(
                   jsonPath("$.message", is(ErrorMessagesEnum.MERCHANT_NOT_FOUND.getMessage())))
               .andDo(print());
    }

    @Test
    public void test_insertOffer() throws Exception {
        final Long merchantId = new Random().nextLong();
        final List<ProductItemRequest> productItemRequests = Arrays.asList(
            getProductItemRequest());
        final OfferRequest offerRequest = getOfferRequest(productItemRequests);
        when(offerService.insertOfferForMerchant(merchantId.toString(), offerRequest)).thenReturn(
            getOfferResponse(offerRequest.getCurrency(), offerRequest.getDescription()));
        mockMvc.perform(
            post(TestConstants.OFFER_CONTROLLER_URI + "/" + "merchants/" + merchantId).headers(
                getHttpHeaders())
                                                                                      .content(
                                                                                          asJsonString(
                                                                                              offerRequest)))
               .andExpect(status().isCreated())
               .andDo(print());
    }

    @Test
    public void test_getActiveOffersForMerchant() throws Exception {
        final Long merchantId = new Random().nextLong();
        final List<OfferResponse>
            offerResponses =
            Arrays.asList(getOfferResponse(getRandomString(), getRandomString()));
        when(offerService.getOfferByMerchantAndStatus(merchantId.toString(),
                                                      OfferStatus.ACTIVE.toString())).thenReturn(
            offerResponses);
        mockMvc.perform(
            get(TestConstants.OFFER_CONTROLLER_URI + "/" + "merchants/" + merchantId).headers(
                getHttpHeaders()))
               .andExpect(status().isOk())
               .andDo(print())
               .andExpect(jsonPath("$[0].currency",
                                   is(offerResponses.get(0)
                                                    .getCurrency())))
               .andExpect(jsonPath("$[0].description",
                                   is(offerResponses.get(0)
                                                    .getDescription())));
    }

    private OfferResponse getOfferResponse(final String currency, final String description) {
        return OfferResponse.builder()
                            .currency(currency)
                            .description(description)
                            .status(OfferStatus.ACTIVE.toString())
                            .build();
    }

    private OfferRequest getOfferRequest(final List<ProductItemRequest> productItemRequests) {
        return OfferRequest.builder()
                           .description(getRandomString())
                           .currency(getRandomString())
                           .price(new BigDecimal(5))
                           .productItemRequests(productItemRequests)
                           .build();
    }

    private ProductItemRequest getProductItemRequest() {
        return ProductItemRequest.builder()
                                 .productDescription(getRandomString())
                                 .productType(getRandomString())
                                 .build();
    }

    private String getRandomString() {
        return RandomStringUtils.random(5);
    }

    private HttpHeaders getHttpHeaders() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return httpHeaders;
    }

    public String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
