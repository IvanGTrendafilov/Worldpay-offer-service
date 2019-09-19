package com.trendafilov.ivan.worldpay.offerservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendafilov.ivan.worldpay.offerservice.TestConstants;
import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.MerchantRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.MerchantResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Merchant;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        final MerchantRequest merchantRequest = createMerchantRequest();
        performSaveMerchant(merchantRequest);
    }

    private void performSaveMerchant(final MerchantRequest merchantRequest) throws Exception {
        mockMvc.perform(
            post(TestConstants.MERCHANT_CONTROLLER_URI).headers(getHttpHeaders())
                                                       .content(asJsonString(merchantRequest)))
               .andExpect(status().isCreated());
    }


    @Test
    public void test_getAllMerchnats() throws Exception {
//        final MerchantRequest merchantRequest = createMerchantRequest();
//        mockMvc.perform(
//            post(Constants.MERCHANT_CONTROLLER_URI).headers(getHttpHeaders())
//                                                  .content(asJsonString(merchantRequest)))
//               .andExpect(status().isCreated());
//        final MerchantRequest merchantRequest = createMerchantRequest();
//        performSaveMerchant(merchantRequest);
        mockMvc.perform(get(TestConstants.MERCHANT_CONTROLLER_URI).headers(getHttpHeaders()))
               .andExpect(status().isOk())
               .andDo(MockMvcResultHandlers.print());
//               .andExpect(jsonPath("$[0].firstName", Is.is(merchantRequest.getFirstName())))
//               .andExpect(jsonPath("$[0].lastName", Is.is(merchantRequest.getLastName())))
//               .andExpect(jsonPath("$[0].firstName", Is.is(merchantRequest.getDepartment())));

    }

    @Test
    public void test_getMerchantById() throws Exception {
        final Merchant
            merchant =
            Merchant.builder()
                    .merchantId(123L)
                    .firstName(RandomStringUtils.random(5))
                    .lastName(RandomStringUtils.random(5))
                    .department(RandomStringUtils.random(5))
                    .build();
        when(merchantService.findMerchantByMerchantId("123"))
            .thenReturn(merchant);
        final MerchantResponse
            merchantResponse =
            MerchantResponse.builder()
                            .merchantId(merchant.getMerchantId())
                            .firstName(merchant.getFirstName())
                            .department(merchant.getDepartment())
                            .build();
        when(merchantMapper.convertMerchantToResponse(merchant))
            .thenReturn(merchantResponse);
        mockMvc.perform(
            get(TestConstants.MERCHANT_CONTROLLER_URI + "/123").headers(getHttpHeaders()))
               .andExpect(status().isOk())
               .andDo(MockMvcResultHandlers.print());
    }

    private MerchantRequest createMerchantRequest() {
        return MerchantRequest.builder()
                              .firstName(RandomStringUtils.random(5))
                              .lastName(RandomStringUtils.random(5))
                              .department(RandomStringUtils.random(5))
                              .build();
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
