package com.aspire.common.exceptions;

import org.springframework.http.HttpStatus;

public class RepaymentAlreadyPaidException extends AspireApiException {
    public RepaymentAlreadyPaidException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
