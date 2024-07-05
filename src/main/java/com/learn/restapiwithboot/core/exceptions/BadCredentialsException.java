package com.learn.restapiwithboot.core.exceptions;

import com.learn.restapiwithboot.core.enums.ErrorMessage;

public class BadCredentialsException extends BaseException {

    public BadCredentialsException(ErrorMessage errorType) {
        super(errorType);
    }

    public BadCredentialsException(Integer status) {
        super(status);
    }

    public BadCredentialsException(String message) {
        super(message);
    }

    public BadCredentialsException(Integer status, String message) {
        super(status, message);
    }
}
