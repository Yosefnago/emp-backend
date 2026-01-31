package com.ms.sw.exception.auth;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends AuthException {

    private static final String ERROR_CODE = "AUTH_004";
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public InvalidCredentialsException(String message) {
        super(message, STATUS, ERROR_CODE);
    }
}
