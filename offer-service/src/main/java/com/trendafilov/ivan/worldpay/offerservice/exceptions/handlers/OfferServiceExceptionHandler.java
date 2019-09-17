package com.trendafilov.ivan.worldpay.offerservice.exceptions.handlers;

import com.trendafilov.ivan.worldpay.offerservice.dtos.requests.response.ErrorResponse;
import com.trendafilov.ivan.worldpay.offerservice.exceptions.OfferServiceException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OfferServiceExceptionHandler {

    @ExceptionHandler(OfferServiceException.class)
    public final ResponseEntity handleSpecificExceptions(final OfferServiceException exception) {
        final HttpStatus httpStatus = HttpStatus.valueOf(exception.getStatusCode());
        final ErrorResponse
            errorResponse =
            ErrorResponse.builder()
                         .message(exception.getMessage())
                         .build();
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
