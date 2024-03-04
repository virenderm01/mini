package com.aspire.common.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AspireApiException{
    public UserNotFoundException(String message, HttpStatus errorCode) {
        super(message, errorCode);
    }
}
