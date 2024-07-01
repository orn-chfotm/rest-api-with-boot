package com.learn.restapiwithboot.core.exceptions;

import com.learn.restapiwithboot.core.enums.Exceptions;

public class TokenExpiredException extends RuntimeException {
    private final Exceptions EXCEPTION = Exceptions.INVALID_JWT_TOKEN;

    public TokenExpiredException(String message) {
        super(message);
    }

    public Exceptions getException() {
        return EXCEPTION;
    }

    public Integer getStatus() {
        return EXCEPTION.getStatus();
    }

    public String getMessage() {
        return EXCEPTION.getMessage();
    }

    public String getDetailMessage() {
        return super.getMessage();
    }
}
