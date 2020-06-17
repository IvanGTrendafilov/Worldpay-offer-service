package com.trendafilov.ivan.worldpay.offerservice.services.impl;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.OfferRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.ProductItemRequest;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.MerchantResponse;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.OfferResponse;
import com.trendafilov.ivan.worldpay.offerservice.dtos.response.ProductItemResponse;
import com.trendafilov.ivan.worldpay.offerservice.entities.Merchant;
import com.trendafilov.ivan.worldpay.offerservice.entities.Offer;
import com.trendafilov.ivan.worldpay.offerservice.entities.Student;
import com.trendafilov.ivan.worldpay.offerservice.enums.ErrorMessagesEnum;
import com.trendafilov.ivan.worldpay.offerservice.enums.OfferStatus;
import com.trendafilov.ivan.worldpay.offerservice.exceptions.OfferServiceException;
import com.trendafilov.ivan.worldpay.offerservice.mappers.OfferMapper;
import com.trendafilov.ivan.worldpay.offerservice.mappers.StudentMapper;
import com.trendafilov.ivan.worldpay.offerservice.repositories.OfferRepository;
import com.trendafilov.ivan.worldpay.offerservice.services.IMerchantService;
import com.trendafilov.ivan.worldpay.offerservice.services.IOfferService;
import com.trendafilov.ivan.worldpay.offerservice.services.IProductService;
import com.trendafilov.ivan.worldpay.offerservice.services.IStudentService;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OfferService implements IOfferService {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final IMerchantService merchantService;
    private final IProductService productService;
    private final IStudentService studentService;
    private final StudentMapper studentMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OfferResponse insertOfferForMerchant(final String merchantId,
                                                final OfferRequest offerRequest)
        throws OfferServiceException {
        log.debug("Insert Merchant Offer with MerchantId: {} and OfferRequest: {}", merchantId,
                  offerRequest);
        final Merchant merchantByMerchantId = merchantService.findMerchantByMerchantId(merchantId);
        final Offer
            offerDb =
            offerMapper.convertOfferRequestToJpaEntity(offerRequest, merchantByMerchantId);
        final Offer savedOffer = offerRepository.save(offerDb);
        final List<ProductItemRequest> productItemRequests = offerRequest.getProductItemRequests();
        final List<ProductItemResponse>
            productItemResponses =
            productService.saveProductItemsForOffer(offerDb, productItemRequests);
        final MerchantResponse
            merchantResponseByMerchantEntity =
            merchantService.getMerchantResponseByMerchantEntity(
                merchantByMerchantId);
        return offerMapper.convertFullOfferToResponse(savedOffer, productItemResponses,
                                                      merchantResponseByMerchantEntity, null);
    }

    @Override
    public List<OfferResponse> getOfferByMerchantAndStatus(final String merchantId,
                                                           final String offerStatus)
        throws OfferServiceException {
        final Merchant merchantByMerchantId = merchantService.findMerchantByMerchantId(merchantId);
        final List<Offer>
            offersByMerchantAnAndStatus =
            offerRepository.findOffersByMerchantMerchantIdAndStatus(
                merchantByMerchantId.getMerchantId(),
                offerStatus.toString());
        final List<OfferResponse> offerResponses = new ArrayList<>();
        offersByMerchantAnAndStatus.stream()
                                   .forEach(offer -> {
                                       Student
                                           studentByStudentId = null;
                                       if (offer.getStudent() != null) {
                                           studentByStudentId =
                                               studentService.findStudentByStudentId(
                                                   offer.getStudent()
                                                        .getStudentId()
                                                        .toString());
                                       }
                                       final OfferResponse
                                           offerResponse =
                                           getOfferResponse(merchantByMerchantId, offer,
                                                            studentByStudentId);
                                       offerResponses.add(offerResponse);
                                   });
        return offerResponses;
    }

    private OfferResponse getOfferResponse(final Merchant merchantByMerchantId, final Offer offer,
                                           final Student studentByStudentId) {
        final List<ProductItemResponse>
            allProductItemsResponsesForOffer =
            productService.getAllProductItemsResponsesForOffer(
                offer);
        return offerMapper.convertFullOfferToResponse(offer,
                                                      allProductItemsResponsesForOffer,
                                                      merchantService.getMerchantResponseByMerchantEntity(
                                                          merchantByMerchantId),
                                                      studentMapper.convertStudentEntityToResponse(
                                                          studentByStudentId));
    }

    @Override
    public void cancelMerchantOffer(final String merchantId, final String offerId)
        throws OfferServiceException {
        final Merchant merchantByMerchantId = merchantService.findMerchantByMerchantId(merchantId);
        final long providedOfferId = 0L;
        final Offer offer = getOffer(merchantId, offerId);
        if (!merchantByMerchantId.getMerchantId()
                                 .equals(offer.getMerchant()
                                              .getMerchantId())) {
            log.error("Wrong merchant with Id: {} trying to cancel offer with Id: {}", merchantId,
                      offerId);
            throwOfferServiceException(ErrorMessagesEnum.MERCHANT_DOES_NOT_OWN_OFFER);
        }
        offer.setStatus(OfferStatus.CANCELLED.toString());
        offer.setExpireDate(new Date());
        offerRepository.save(offer);
        log.debug("Offer with Id: {} was canceled");
    }

    private Offer getOffer(final String id, final String offerId) {
        final long providedOfferId;
        Offer offer = null;
        try {
            providedOfferId = Long.parseLong(offerId);
            final Optional<Offer>
                offerOptional =
                offerRepository.findById(providedOfferId);
            if (!offerOptional.isPresent()) {
                log.error("Offer with Id: {} for id : {} was not found", offerId,
                          id);
                throwOfferServiceException(ErrorMessagesEnum.OFFER_NOT_FOUND);
            }
            offer = offerOptional.get();
        } catch (final NumberFormatException e) {
            log.error("Random string {} is provided during cancel merchant offer", offerId);
            throwOfferServiceException(ErrorMessagesEnum.OFFER_NOT_FOUND);
        }
        return offer;
    }

    @Override
    public void changeOfferStatusForStudent(final String studentId, final String offerId,
                                            final OfferStatus offerStatus) {
        final Student studentByStudentId = studentService.findStudentByStudentId(studentId);
        final Offer offer = getOffer(studentId, offerId);
        if (offer.getStudent() == null || !studentByStudentId.getStudentId()
                                                             .equals(offer.getStudent()
                                                                          .getStudentId())) {
            log.error("Wrong student with Id: {} trying to accept offer with Id: {}", studentId,
                      offerId);
            throwOfferServiceException(ErrorMessagesEnum.STUDENT_DOES_NOT_HAVE_SUCH_OFFER);
        }
        offer.setStatus(offerStatus.toString());
        switch (offerStatus) {
            case ACTIVE:
                offer.setExpireDate(DateUtils.addDays(new Date(), 10));
                break;
            case ACCEPTED:
                offer.setExpireDate(DateUtils.addYears(new Date(), 5));
                break;
            case CANCELLED:
            case DECLINED:
                offer.setExpireDate(new Date());
                break;
        }
        offerRepository.save(offer);
    }

    @Override
    public OfferResponse assignStudentToOffer(final String studentId, final String offerId,
                                              final String merchantId) {
        final Student studentByStudentId = studentService.findStudentByStudentId(studentId);
        final Offer offer = getOffer(studentId, offerId);
        final List<Offer>
            offersByStudentStudentIdAndStatus =
            offerRepository.findOffersByStudentStudentIdAndStatus(Long.parseLong(studentId),
                                                                  OfferStatus.ACCEPTED.toString());
        if (!offersByStudentStudentIdAndStatus.isEmpty()) {
            throwOfferServiceException(ErrorMessagesEnum.STUDENT_HAS_ACCEPTED_OFFER_ALREADY);
        }
        offer.setStudent(studentByStudentId);
        offerRepository.save(offer);
        final Merchant
            merchantByMerchantId =
            merchantService.findMerchantByMerchantId(merchantId);
        final List<ProductItemResponse>
            productItemResponses =
            productService.getAllProductItemsResponsesForOffer(offer);
        final MerchantResponse
            merchantResponseByMerchantEntity =
            merchantService.getMerchantResponseByMerchantEntity(
                merchantByMerchantId);
        return offerMapper.convertFullOfferToResponse(offer, productItemResponses,
                                                      merchantResponseByMerchantEntity,
                                                      studentMapper.convertStudentEntityToResponse(
                                                          studentByStudentId));
    }

    @Override
    public List<OfferResponse> getAllOffersToStudent(final String studentId) {
        final Student studentByStudentId = studentService.findStudentByStudentId(studentId);
        final List<Offer>
            offers =
            offerRepository.findOffersByStudentStudentId(studentByStudentId.getStudentId());
        final List<OfferResponse> offerResponses = new ArrayList<>();
        offers.stream()
              .forEach(offer -> {
                  Merchant
                      merchantByMerchantId = null;
                  if (offer.getMerchant() != null) {
                      merchantByMerchantId =
                          merchantService.findMerchantByMerchantId(offer.getMerchant()
                                                                        .getMerchantId()
                                                                        .toString());
                  }
                  final OfferResponse
                      offerResponse =
                      getOfferResponse(merchantByMerchantId, offer, studentByStudentId);
                  offerResponses.add(offerResponse);
              });
        return offerResponses;
    }

    private void throwOfferServiceException(final ErrorMessagesEnum errorMessagesEnum)
        throws OfferServiceException {
        throw new OfferServiceException(errorMessagesEnum.getMessage(),
                                        HttpStatus.BAD_REQUEST.value());
    }
}
