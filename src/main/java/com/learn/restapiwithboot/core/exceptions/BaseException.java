package com.learn.restapiwithboot.core.exceptions;

import com.learn.restapiwithboot.core.exceptions.enums.ErrorMessage;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private Integer status;

    /* Default Exception */
    public BaseException(ErrorMessage errorType) {
        super(errorType.getMessage());
        this.status = errorType.getStatus();
    }

    /* Custom Exception */
    public BaseException(Integer status) {
        this.status = status;
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(Integer status, String message) {
        super(message);
        this.status = status;
    }
}
