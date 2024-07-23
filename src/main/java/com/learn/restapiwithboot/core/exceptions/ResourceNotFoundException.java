package com.learn.restapiwithboot.core.exceptions;

import com.learn.restapiwithboot.core.exceptions.base.BaseException;
import com.learn.restapiwithboot.core.exceptions.enums.ErrorMessage;

public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(ErrorMessage errorType) {
        super(errorType);
    }

    public ResourceNotFoundException(Integer status) {
        super(status);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(Integer status, String message) {
        super(status, message);
    }
}
