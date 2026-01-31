package com.ms.sw.exception.auth;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends AuthException {

    private static final String ERROR_CODE = "AUTH_001";
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;

    public UserAlreadyExistsException(String message) {
        super(message, STATUS, ERROR_CODE);
    }
}
