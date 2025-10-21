package com.ms.sw.exception.global;

import com.ms.sw.exception.auth.InvalidCredentialsException;
import com.ms.sw.exception.auth.UserAlreadyExistsException;
import com.ms.sw.exception.auth.UserNotFoundException;
import com.ms.sw.exception.employees.EmployeesNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserExists(UserAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<?> handleInvalidCredentials(InvalidCredentialsException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(EmployeesNotFoundException.class)
    public ResponseEntity<?> handleEmployeeByIdNotFound(EmployeesNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
    }

}
