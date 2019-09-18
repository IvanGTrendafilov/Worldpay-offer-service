package com.trendafilov.ivan.worldpay.offerservice.exceptions;

public class OfferServiceException extends Exception {

    private final Integer statusCode;

    public OfferServiceException(final Integer statusCode) {
        this.statusCode = statusCode;
    }

    public OfferServiceException(final String message, final Integer statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public Integer getStatusCode() {
        return statusCode;
    }
}
