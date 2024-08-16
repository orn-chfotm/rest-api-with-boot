package com.learn.restapiwithboot.core.exceptions.enums;

import org.springframework.http.HttpStatus;

public interface ErrorType {

    HttpStatus getStatus();
    String getMessage();
}
