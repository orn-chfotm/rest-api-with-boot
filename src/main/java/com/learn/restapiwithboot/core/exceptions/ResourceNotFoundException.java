package com.learn.restapiwithboot.core.exceptions;

import com.learn.restapiwithboot.core.enums.Exceptions;

public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(Integer status) {
        super(status);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Integer status) {
        super(message, status);
    }
}
