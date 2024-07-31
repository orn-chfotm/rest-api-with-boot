package com.learn.restapiwithboot.core.exceptions.impl;

import com.learn.restapiwithboot.core.exceptions.BaseException;
import com.learn.restapiwithboot.core.exceptions.enums.ErrorMessage;

public class ApplicantCountException extends BaseException {
    
    public ApplicantCountException(ErrorMessage errorType) {
        super(errorType);
    }

    public ApplicantCountException(Integer status) {
        super(status);
    }

    public ApplicantCountException(String message) {
        super(message);
    }

    public ApplicantCountException(Integer status, String message) {
        super(status, message);
    }
}
