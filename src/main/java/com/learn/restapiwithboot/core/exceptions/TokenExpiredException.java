package com.learn.restapiwithboot.core.exceptions;

import com.learn.restapiwithboot.core.enums.Exceptions;

public class TokenExpiredException extends BaseException {

    public TokenExpiredException(Integer status) {
        super(status);
    }

    public TokenExpiredException(String message) {
        super(message);
    }

    public TokenExpiredException(String message, Integer status) {
        super(message, status);
    }
}
