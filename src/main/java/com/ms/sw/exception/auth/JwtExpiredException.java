package com.ms.sw.exception.auth;


public class JwtExpiredException extends JwtException {

    public JwtExpiredException(String message) {
        super(message);
    }
}
