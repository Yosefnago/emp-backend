package com.ms.sw.exception.employees;

import com.ms.sw.exception.base.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * Base class for all employee-related exceptions.
 */
public abstract class EmployeeException extends BusinessException {

    protected EmployeeException(String message, HttpStatus httpStatus, String errorCode) {
        super(message, httpStatus, errorCode);
    }

    protected EmployeeException(String message, Throwable cause, HttpStatus httpStatus, String errorCode) {
        super(message, cause, httpStatus, errorCode);
    }
}
