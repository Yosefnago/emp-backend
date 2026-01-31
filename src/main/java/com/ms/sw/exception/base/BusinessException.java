package com.ms.sw.exception.base;

import org.springframework.http.HttpStatus;

/**
 * Base class for business logic exceptions.
 * These exceptions represent violations of business rules or invalid operations.
 */
public abstract class BusinessException extends BaseException {

    protected BusinessException(String message, HttpStatus httpStatus, String errorCode) {
        super(message, httpStatus, errorCode);
    }

    protected BusinessException(String message, Throwable cause, HttpStatus httpStatus, String errorCode) {
        super(message, cause, httpStatus, errorCode);
    }
}
