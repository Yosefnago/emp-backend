package com.ms.sw.exception.auth;

import com.ms.sw.exception.base.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * Base class for all authentication and authorization related exceptions.
 */
public abstract class AuthException extends BusinessException {

    protected AuthException(String message, HttpStatus httpStatus, String errorCode) {
        super(message, httpStatus, errorCode);
    }

    protected AuthException(String message, Throwable cause, HttpStatus httpStatus, String errorCode) {
        super(message, cause, httpStatus, errorCode);
    }
}
