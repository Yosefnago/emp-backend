package com.ms.sw.exception.handler;

import com.ms.sw.exception.auth.JwtExpiredException;
import com.ms.sw.exception.auth.JwtInvalidException;
import com.ms.sw.exception.employee.AddEmployeeException;
import com.ms.sw.exception.employee.EmployeeNotFoundException;
import com.ms.sw.exception.user.EmailAlreadyExistsException;
import com.ms.sw.exception.user.InvalidCredentialsException;
import com.ms.sw.exception.user.UserAlreadyExistsException;
import com.ms.sw.exception.user.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.util.HtmlUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class ExceptionController {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @ExceptionHandler({UserNotFoundException.class, EmployeeNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    // 409 - Conflict
    @ExceptionHandler({UserAlreadyExistsException.class, EmailAlreadyExistsException.class})
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    // 401 - Unauthorized (JWT)
    @ExceptionHandler({JwtExpiredException.class, JwtInvalidException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorized(RuntimeException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    // 400 - Bad Request
    @ExceptionHandler({AddEmployeeException.class, InvalidCredentialsException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                HtmlUtils.htmlEscape(request.getRequestURI()),
                LocalDateTime.now().format(FORMATTER)
        );

        return new ResponseEntity<>(error, status);
    }
}
