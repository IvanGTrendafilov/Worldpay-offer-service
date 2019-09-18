package com.trendafilov.ivan.worldpay.offerservice.enums;

public enum ErrorMessagesEnum {

    MERCHANT_NOT_FOUND("Merchant not found!"),
    PRICE_LESS_THAN_ZERO("Offer Price can not be less than 0!");

    private final String message;

    private ErrorMessagesEnum(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
