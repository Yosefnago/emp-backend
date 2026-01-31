package com.ms.sw.exception.employees;

import org.springframework.http.HttpStatus;

public class EmployeeNotFoundException extends EmployeeException {

    private static final String ERROR_CODE = "EMP_001";
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    public EmployeeNotFoundException(String message) {
        super(message, STATUS, ERROR_CODE);
    }
}
