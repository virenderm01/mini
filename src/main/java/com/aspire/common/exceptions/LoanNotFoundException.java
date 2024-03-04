package com.aspire.common.exceptions;

import org.springframework.http.HttpStatus;

public class LoanNotFoundException extends AspireApiException {
    public LoanNotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
