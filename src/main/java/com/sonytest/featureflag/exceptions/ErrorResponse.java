package com.sonytest.featureflag.exceptions;

import java.util.Date;

// CLOVER:OFF
public class ErrorResponse {

    private String messageCode;
    private String messageDetails;

    public ErrorResponse(String messageCode, String messageDetails) {
        this.messageCode = messageCode;
        this.messageDetails = messageDetails;
    }

    public String getMessageDetails() {
        return messageDetails;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public Date getTimeStamp() {
        return new Date(System.currentTimeMillis());
    }
}
