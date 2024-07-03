package com.learn.restapiwithboot.core.exceptions;

import com.learn.restapiwithboot.core.enums.Exceptions;

public class TokenInvalidException extends BaseException {

    public TokenInvalidException(Integer status) {
        super(status);
    }

    public TokenInvalidException(String message) {
        super(message);
    }

    public TokenInvalidException(String message, Integer status) {
        super(message, status);
    }
}
