package com.learn.restapiwithboot.core.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BasicResponse {

   private final HttpStatus statusCode;
   private final String message;

    public BasicResponse(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
