package com.learn.restapiwithboot.core.exceptions.impl;

import com.learn.restapiwithboot.core.exceptions.BaseException;
import com.learn.restapiwithboot.core.exceptions.enums.ErrorMessage;

public class AccountExistenceException extends BaseException {

    public AccountExistenceException(ErrorMessage errorType) {
        super(errorType);
    }

    public AccountExistenceException(Integer status) {
        super(status);
    }

    public AccountExistenceException(String message) {
        super(message);
    }

    public AccountExistenceException(Integer status, String message) {
        super(status, message);
    }
}
