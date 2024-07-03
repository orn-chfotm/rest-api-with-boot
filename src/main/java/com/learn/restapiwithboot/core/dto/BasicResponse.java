package com.learn.restapiwithboot.core.dto;

import lombok.Getter;

@Getter
public abstract class BasicResponse {

   private final Integer statusCode;

   private final String message;

    public BasicResponse(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
