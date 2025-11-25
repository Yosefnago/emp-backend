package com.ms.sw.exception.global;

import com.ms.sw.exception.auth.EmailAlreadyExistsException;
import com.ms.sw.exception.auth.InvalidCredentialsException;
import com.ms.sw.exception.auth.UserAlreadyExistsException;
import com.ms.sw.exception.auth.UserNotFoundException;
import com.ms.sw.exception.document.DocumentNotFoundException;
import com.ms.sw.exception.employees.AddEmployeeException;
import com.ms.sw.exception.employees.EmployeesNotFoundException;
import com.ms.sw.exception.salary.SalaryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserAlreadyExistsException.class, EmailAlreadyExistsException.class})
    public ResponseEntity<Map<String, String>> handleConflictExceptions() {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409
                .body(Map.of("error", "The requested resource already exists."));
    }

    @ExceptionHandler({UserNotFoundException.class, InvalidCredentialsException.class})
    public ResponseEntity<Map<String, String>> handleAuthFailures() {
        // Use a generic message for security
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED) // 401
                .body(Map.of("error", "Authentication failed. Invalid username or password."));
    }

    @ExceptionHandler({EmployeesNotFoundException.class, SalaryNotFoundException.class, DocumentNotFoundException.class})
    public ResponseEntity<Map<String, String>> handleNotFoundExceptions() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // 404
                .body(Map.of("error", "The requested resource was not found."));
    }

    @ExceptionHandler(AddEmployeeException.class)
    public ResponseEntity<Map<String, String>> handleBadRequestExceptions() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 400
                .body(Map.of("error", "Invalid input or data provided. Please check your request."));
    }

}
