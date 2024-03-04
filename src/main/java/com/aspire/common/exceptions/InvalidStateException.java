package com.aspire.common.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidStateException extends AspireApiException {
    public InvalidStateException(String errorMessage) {
        super(errorMessage, HttpStatus.BAD_REQUEST);
    }
    public InvalidStateException(String message, HttpStatus errorCode) {
        super(message, errorCode);
    }
}
