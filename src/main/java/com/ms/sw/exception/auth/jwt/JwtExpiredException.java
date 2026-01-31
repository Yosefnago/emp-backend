package com.ms.sw.exception.auth.jwt;

import org.springframework.http.HttpStatus;

public class JwtExpiredException extends JwtException {

    private static final String ERROR_CODE = "JWT_001";
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public JwtExpiredException(String message) {
        super(message, STATUS, ERROR_CODE);
    }

    public JwtExpiredException(String message, Throwable cause) {
        super(message, cause, STATUS, ERROR_CODE);
    }
}
