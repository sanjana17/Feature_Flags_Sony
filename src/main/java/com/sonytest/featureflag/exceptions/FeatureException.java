package com.sonytest.featureflag.exceptions;

import org.springframework.http.HttpStatus;

public class FeatureException extends Exception {

    private final String messageCode;
    private final HttpStatus httpStatus;

    public FeatureException(String messageCode, String messageDetails, HttpStatus httpStatus) {
        super(messageDetails);

        this.messageCode = messageCode;
        this.httpStatus = httpStatus;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
