package com.learn.restapiwithboot.core.exceptions.exception.impl;

import com.learn.restapiwithboot.core.exceptions.enums.ErrorType;
import com.learn.restapiwithboot.core.exceptions.exception.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BasicException extends BaseException {

    public BasicException(ErrorType errorType) {
        super(errorType);
    }

    @Override
    public HttpStatus getStatus() {
        return super.getErrorType().getStatus();
    }

    @Override
    public String getMessage() {
        return super.getErrorType().getMessage();
    }
}
