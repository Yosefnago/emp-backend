package com.ms.sw.exception.auth;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends AuthException {

    private static final String ERROR_CODE = "AUTH_002";
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;

    public EmailAlreadyExistsException(String message) {
        super(message, STATUS, ERROR_CODE);
    }
}
