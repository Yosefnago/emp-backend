package com.ms.sw.exception.employees;

import com.ms.sw.exception.employees.EmployeeException;
import org.springframework.http.HttpStatus;

public class AddEmployeeException extends EmployeeException {

    private static final String ERROR_CODE = "EMP_002";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public AddEmployeeException(String message) {
        super(message, STATUS, ERROR_CODE);
    }
}
