package com.trendafilov.ivan.worldpay.offerservice.enums;

public enum ErrorMessagesEnum {

    MERCHANT_NOT_FOUND("Merchant not found!"),
    PRICE_LESS_THAN_ZERO("Offer Price can not be less than 0!"),
    OFFER_NOT_FOUND("Offer not found!"),
    MERCHANT_NAME_EMPTY("Merchant name can not be empty!"),
    MERCHANT_DEPARTMENT_EMPTY("Merchant department can not be empty!"),
    MERCHANT_DOES_NOT_OWN_OFFER("Merchant does not own such Offer!");

    private final String message;

    private ErrorMessagesEnum(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
