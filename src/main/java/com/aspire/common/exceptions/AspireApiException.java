package com.aspire.common.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class AspireApiException extends Exception {
    private String message;
    private HttpStatus errorCode;

    public AspireApiException(String message, HttpStatus errorCode) {
        super();
        this.message = message;
        this.errorCode = errorCode;
    }
}
