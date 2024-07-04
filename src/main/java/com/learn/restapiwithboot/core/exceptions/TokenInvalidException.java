package com.learn.restapiwithboot.core.exceptions;

import com.learn.restapiwithboot.core.enums.ErrorMessage;

public class TokenInvalidException extends BaseException {

    public TokenInvalidException(ErrorMessage errorType) {
        super(errorType);
    }

    public TokenInvalidException(Integer status) {
        super(status);
    }

    public TokenInvalidException(String message) {
        super(message);
    }

    public TokenInvalidException(Integer status, String message) {
        super(status, message);
    }
}