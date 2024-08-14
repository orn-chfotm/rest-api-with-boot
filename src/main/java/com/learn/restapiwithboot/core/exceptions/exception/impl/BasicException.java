package com.learn.restapiwithboot.core.exceptions.exception.impl;

import com.learn.restapiwithboot.core.exceptions.enums.ErrorType;
import com.learn.restapiwithboot.core.exceptions.exception.BaseException;
import lombok.Getter;

@Getter
public class BasicException extends BaseException {

    public BasicException(ErrorType errorType) {
        super(errorType);
    }
}
