package com.ms.sw.exception.auth;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AuthException {

    private static final String ERROR_CODE = "AUTH_003";
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    public UserNotFoundException(String message) {
        super(message, STATUS, ERROR_CODE);
    }
}
