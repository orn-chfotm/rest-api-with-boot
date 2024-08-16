package com.learn.restapiwithboot.core.exceptions.exception.ext;

import com.learn.restapiwithboot.core.exceptions.enums.ErrorType;
import com.learn.restapiwithboot.core.exceptions.exception.BaseException;

public class BasicException extends BaseException {

    public BasicException(ErrorType errorType) {
        super(errorType);
    }
}
