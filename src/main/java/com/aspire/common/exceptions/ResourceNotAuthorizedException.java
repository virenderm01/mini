package com.aspire.common.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotAuthorizedException extends AspireApiException {
    public ResourceNotAuthorizedException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
