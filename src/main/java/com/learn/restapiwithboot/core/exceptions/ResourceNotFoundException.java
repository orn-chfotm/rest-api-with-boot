package com.learn.restapiwithboot.core.exceptions;

import com.learn.restapiwithboot.core.enums.Exceptions;

public class ResourceNotFoundException extends RuntimeException {

    private final Exceptions EXCEPTIONS = Exceptions.RESOURCE_NOT_FOUND;

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public Exceptions getException() {
        return EXCEPTIONS;
    }

    public Integer getStatus() {
        return EXCEPTIONS.getStatus();
    }

    public String getMessage() {
        return EXCEPTIONS.getMessage();
    }

    public String getDetailMessage() {
        return super.getMessage();
    }
}
