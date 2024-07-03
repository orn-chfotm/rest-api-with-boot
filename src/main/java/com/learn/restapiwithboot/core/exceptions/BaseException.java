package com.learn.restapiwithboot.core.exceptions;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private Integer status;

    public BaseException(Integer status) {
        this.status = status;
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Integer status) {
        super(message);
        this.status = status;
    }
}
