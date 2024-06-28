package com.learn.restapiwithboot.core.enums;

import lombok.Getter;

public enum Exceptions {

    INVALID_INPUT_VALUE(400, "Invalid Input Value"),
    INVALID_TYPE_VALUE(400, "Invalid Type Value"),
    INVALID_INPUT_FORMAT(400, "Invalid Input Format"),
    INVALID_TOKEN(401, "Invalid Token"),
    EXPIRED_TOKEN(401, "Expired Token"),
    INVALID_REFRESH_TOKEN(401, "Invalid Refresh Token"),
    INVALID_JWT_TOKEN(401, "Invalid JWT Token"),
    INVALID_JWT_KEY(401, "Invalid JWT Key"),
    NOT_FOUND(404, "Not Found"),
    RESOURCE_NOT_FOUND(404, "Resource Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    INTERNAL_SERVER_ERROR(500, "Server Error")
    ;

    private final Integer status;

    private final String message;

    Exceptions(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return this.status;
    }
    public String getMessage() {
        return message;
    }
}
