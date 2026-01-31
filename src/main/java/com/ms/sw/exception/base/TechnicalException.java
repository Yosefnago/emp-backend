package com.ms.sw.exception.base;

import org.springframework.http.HttpStatus;

/**
 * Base class for technical exceptions.
 * These exceptions represent system-level errors like JWT parsing, database connectivity, etc.
 */
public abstract class TechnicalException extends BaseException {

    protected TechnicalException(String message, HttpStatus httpStatus, String errorCode) {
        super(message, httpStatus, errorCode);
    }

    protected TechnicalException(String message, Throwable cause, HttpStatus httpStatus, String errorCode) {
        super(message, cause, httpStatus, errorCode);
    }
}
