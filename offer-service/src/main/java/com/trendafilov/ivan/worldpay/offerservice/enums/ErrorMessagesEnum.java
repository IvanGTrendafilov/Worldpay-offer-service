package com.trendafilov.ivan.worldpay.offerservice.enums;

public enum ErrorMessagesEnum {
    MERCHANT_NOT_FOUND("Merchant not found!");
    private final String message;

    private ErrorMessagesEnum(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
