package com.ms.sw.exception.auth.jwt;

import org.springframework.http.HttpStatus;

public class JwtInvalidException extends JwtException {

    private static final String ERROR_CODE = "JWT_002";
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public JwtInvalidException(String message) {
        super(message, STATUS, ERROR_CODE);
    }

    public JwtInvalidException(String message, Throwable cause) {
        super(message, cause, STATUS, ERROR_CODE);
    }
}
