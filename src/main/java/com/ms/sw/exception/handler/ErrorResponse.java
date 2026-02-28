package com.ms.sw.exception.handler;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        int status,
        String error,
        String message,
        String path,
        String timestamp
) {}
