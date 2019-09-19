package com.trendafilov.ivan.worldpay.offerservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendafilov.ivan.worldpay.offerservice.TestConstants;
import com.trendafilov.ivan.worldpay.offerservice.services.impl.OfferService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
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
