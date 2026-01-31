package com.ms.sw.exception.auth.jwt;

import com.ms.sw.exception.base.TechnicalException;
import org.springframework.http.HttpStatus;

/**
 * Base class for JWT-related exceptions.
 * JWT exceptions are technical because they deal with token parsing and validation.
 */
public abstract class JwtException extends TechnicalException {

    protected JwtException(String message, HttpStatus httpStatus, String errorCode) {
        super(message, httpStatus, errorCode);
    }

    protected JwtException(String message, Throwable cause, HttpStatus httpStatus, String errorCode) {
        super(message, cause, httpStatus, errorCode);
    }
}
