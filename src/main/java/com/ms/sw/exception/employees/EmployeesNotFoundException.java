package com.ms.sw.exception.employees;

public class EmployeesNotFoundException extends RuntimeException {
    public EmployeesNotFoundException(String message) {
        super(message);
    }
}
