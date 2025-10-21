package com.ms.sw.exception.auth;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }

}
