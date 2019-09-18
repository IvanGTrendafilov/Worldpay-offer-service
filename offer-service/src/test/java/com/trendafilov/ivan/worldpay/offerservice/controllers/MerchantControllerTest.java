package com.trendafilov.ivan.worldpay.offerservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.MerchantRequest;
import com.trendafilov.ivan.worldpay.offerservice.mappers.MerchantMapper;
import com.trendafilov.ivan.worldpay.offerservice.services.impl.MerchantService;

import org.apache.commons.lang3.RandomStringUtils;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(controllers = MerchantController.class)
public class MerchantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MerchantService merchantService;

    @MockBean
    private MerchantMapper merchantMapper;

    @Test
    public void test_saveMerchant() throws Exception {
        final MerchantRequest
            merchantRequest =
            MerchantRequest.builder()
                           .firstName(RandomStringUtils.random(5))
                           .lastName(RandomStringUtils.random(5))
                           .department(RandomStringUtils.random(5))
                           .build();
        mockMvc.perform(
            post("/merchant/v1").headers(getHttpHeaders())
                                .content(asJsonString(merchantRequest)))
               .andExpect(status().isCreated());
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
