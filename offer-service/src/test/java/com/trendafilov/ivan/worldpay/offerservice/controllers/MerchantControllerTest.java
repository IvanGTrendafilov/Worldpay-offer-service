package com.trendafilov.ivan.worldpay.offerservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendafilov.ivan.worldpay.offerservice.TestConstants;
import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.MerchantRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.MerchantResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Merchant;
import com.trendafilov.ivan.worldpay.offerservice.enums.ErrorMessagesEnum;
import com.trendafilov.ivan.worldpay.offerservice.exceptions.OfferServiceException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
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
        final MerchantResponse
            merchantResponse =
            getMerchantResponse(new Random().nextLong(), RandomStringUtils.random(5),
                                RandomStringUtils.random(5), RandomStringUtils.random(
                    5));
        final List<MerchantResponse> merchantResponses = Arrays.asList(
            merchantResponse);
        when(merchantService.getAllMerchants()).thenReturn(merchantResponses);
        mockMvc.perform(get(TestConstants.MERCHANT_CONTROLLER_URI).headers(getHttpHeaders()))
               .andExpect(status().isOk())
               .andDo(print())
               .andExpect(jsonPath("$[0].merchantId",
                                   is(merchantResponse.getMerchantId())))
               .andExpect(jsonPath("$[0].firstName",
                                   is(merchantResponse.getFirstName())))
               .andExpect(jsonPath("$[0].lastName", is(merchantResponse.getLastName())))
               .andExpect(jsonPath("$[0].department", is(merchantResponse.getDepartment())));

    }

    @Test
    public void test_getMerchantByIdWithInvalidId() throws Exception {
        final Long merchantId = new Random().nextLong();
        when(merchantService.findMerchantByMerchantId(merchantId
                                                          .toString())).thenThrow(
            new OfferServiceException(ErrorMessagesEnum.MERCHANT_NOT_FOUND.getMessage(),
                                      HttpStatus.BAD_REQUEST.value()));
        mockMvc.perform(
            get(TestConstants.MERCHANT_CONTROLLER_URI + "/" + merchantId).headers(getHttpHeaders()))
               .andExpect(status().isBadRequest())
               .andExpect(
                   jsonPath("$.message", is(ErrorMessagesEnum.MERCHANT_NOT_FOUND.getMessage())))
               .andDo(print());
    }

    @Test
    public void test_getMerchantById() throws Exception {
        final Merchant
            merchant =
            createMerchant();
        when(merchantService.findMerchantByMerchantId(merchant.getMerchantId()
                                                              .toString()))
            .thenReturn(merchant);
        final MerchantResponse
            merchantResponse =
            getMerchantResponse(merchant.getMerchantId(), merchant.getFirstName(),
                                merchant.getLastName(), merchant.getDepartment());
        when(merchantMapper.convertMerchantToResponse(merchant))
            .thenReturn(merchantResponse);
        mockMvc.perform(
            get(TestConstants.MERCHANT_CONTROLLER_URI + "/" + merchant.getMerchantId()
                                                                      .toString()).headers(
                getHttpHeaders()))
               .andExpect(status().isOk())
               .andDo(print())
               .andExpect(jsonPath("$.merchantId",
                                   is(merchantResponse.getMerchantId())))
               .andExpect(jsonPath("$.firstName",
                                   is(merchantResponse.getFirstName())))
               .andExpect(jsonPath("$.lastName", is(merchantResponse.getLastName())))
               .andExpect(jsonPath("$.department", is(merchantResponse.getDepartment())));
    }

    private MerchantResponse getMerchantResponse(final Long merchantId, final String firstName,
                                                 final String lastName,
                                                 final String department) {
        return MerchantResponse.builder()
                               .merchantId(merchantId)
                               .firstName(firstName)
                               .lastName(lastName)
                               .department(department)
                               .build();
    }

    private Merchant createMerchant() {
        return Merchant.builder()
                       .merchantId(new Random(0).nextLong())
                       .firstName(RandomStringUtils.random(5))
                       .lastName(RandomStringUtils.random(5))
                       .department(RandomStringUtils.random(5))
                       .build();
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
