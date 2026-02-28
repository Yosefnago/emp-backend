package com.ms.sw.exception.auth;


/**
 * Base class for JWT-related exceptions.
 * JWT exceptions are technical because they deal with token parsing and validation.
 */
public abstract class JwtException extends RuntimeException {

    public JwtException(String message) {
        super(message);
    }
}
