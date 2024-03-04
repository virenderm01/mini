package com.aspire.common.exceptions;

import org.springframework.http.HttpStatus;

public class RepaymentAmountInvalidException extends AspireApiException {
    public RepaymentAmountInvalidException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
