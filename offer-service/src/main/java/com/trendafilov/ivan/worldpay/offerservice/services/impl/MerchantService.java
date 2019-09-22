package com.trendafilov.ivan.worldpay.offerservice.services.impl;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.MerchantRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.MerchantResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Merchant;
import com.trendafilov.ivan.worldpay.offerservice.enums.ErrorMessagesEnum;
import com.trendafilov.ivan.worldpay.offerservice.exceptions.OfferServiceException;
import com.trendafilov.ivan.worldpay.offerservice.mappers.MerchantMapper;
import com.trendafilov.ivan.worldpay.offerservice.repositories.MerchantRepository;
import com.trendafilov.ivan.worldpay.offerservice.services.IMerchantService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MerchantService implements IMerchantService {

    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;

    @Autowired
    public MerchantService(
        final MerchantRepository merchantRepository,
        final MerchantMapper merchantMapper) {
        this.merchantRepository = merchantRepository;
        this.merchantMapper = merchantMapper;
    }

    @Override
    public Merchant findMerchantByMerchantId(final String merchantId) throws OfferServiceException {
        long providedMerchantId = 0L;
        Merchant merchant = null;
        try {
            providedMerchantId = Long.parseLong(merchantId);
            final Optional<Merchant>
                merchantOptional =
                merchantRepository.findById(providedMerchantId);
            if (!merchantOptional.isPresent()) {
                log.error("findMerchantByMerchantId for merchantId : {} was not found",
                          merchantId);
                return throwOfferServiceException(
                    ErrorMessagesEnum.MERCHANT_NOT_FOUND.getMessage());
            }
            merchant = merchantOptional.get();
        } catch (final NumberFormatException e) {
            log.error("Random string {} is provided during findMerchantByMerchantId", merchantId);
            throwOfferServiceException(ErrorMessagesEnum.MERCHANT_NOT_FOUND.getMessage());
        }
        return merchant;
    }

    private Merchant throwOfferServiceException(final String message) throws OfferServiceException {
        throw new OfferServiceException(message,
                                        HttpStatus.BAD_REQUEST.value());
    }

    @Override
    public MerchantResponse getMerchantResponseByMerchantEntity(final Merchant merchant) {
        return merchantMapper.convertMerchantToResponse(
            merchant);
    }

    @Override
    public List<MerchantResponse> getAllMerchants() {
        final List<Merchant> allMerchants = merchantRepository.findAll();
        return allMerchants.stream()
                           .map(merchant -> getMerchantResponseByMerchantEntity(merchant))
                           .collect(
                               Collectors.toList());
    }

    @Override
    public MerchantResponse createMerchantByMerchantRequest(final MerchantRequest merchantRequest)
        throws OfferServiceException {
        if (StringUtils.isEmpty(merchantRequest.getFirstName())) {
            log.error(
                "Merchant first name is not provided for createMerchantByMerchantRequest with MerchantRequest: {}",
                merchantRequest);
            throwOfferServiceException(ErrorMessagesEnum.MERCHANT_NAME_EMPTY.getMessage());
        }
        if (StringUtils.isEmpty(merchantRequest.getDepartment())) {
            log.error(
                "Merchant department is not provided for createMerchantByMerchantRequest with MerchantRequest: {}",
                merchantRequest);
            throwOfferServiceException(ErrorMessagesEnum.MERCHANT_DEPARTMENT_EMPTY.getMessage());
        }
        Merchant
            merchant =
            merchantMapper.convertMerchantRequestToEntity(merchantRequest);
        merchant = merchantRepository.save(merchant);
        return merchantMapper.convertMerchantToResponse(
            merchant);
    }
}
