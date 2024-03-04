package com.aspire.common.exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends AspireApiException{
    public UserAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);

    }
}
