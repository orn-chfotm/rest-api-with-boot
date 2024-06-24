package com.learn.restapiwithboot.core.enums;

import lombok.Getter;

public enum Exceptions {

    INVALID_INPUT_VALUE(400, "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    INTERNAL_SERVER_ERROR(500, "Server Error"),
    INVALID_TYPE_VALUE(400, "Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "Access is Denied"),
    ENTITY_NOT_FOUND(400, "Entity Not Found"),
    INVALID_INPUT_FORMAT(400, "Invalid Input Format"),
    INVALID_TOKEN(401, "Invalid Token"),
    EXPIRED_TOKEN(401, "Expired Token"),
    INVALID_REFRESH_TOKEN(401, "Invalid Refresh Token"),
    INVALID_JWT_TOKEN(401, "Invalid JWT Token"),
    INVALID_JWT_SIGNATURE(401, "Invalid JWT Signature"),
    INVALID_JWT_CLAIM(401, "Invalid JWT Claim"),
    INVALID_JWT_MALFORMED(401, "Invalid JWT Malformed"),
    INVALID_JWT_UNSUPPORTED(401, "Invalid JWT Unsupported"),
    INVALID_JWT_EXPIRED(401, "Invalid JWT Expired"),
    INVALID_JWT_NOT_BEFORE(401, "Invalid JWT Not Before"),
    INVALID_JWT_AUDIENCE(401, "Invalid JWT Audience"),
    INVALID_JWT_ISSUER(401, "Invalid JWT Issuer"),
    INVALID_JWT_SUBJECT(401, "Invalid JWT Subject"),
    INVALID_JWT_ID(401, "Invalid JWT ID"),
    INVALID_JWT_COMPACT(401, "Invalid JWT Compact"),
    INVALID_JWT_DECODE(401, "Invalid JWT Decode"),
    INVALID_JWT_ENCODE(401, "Invalid JWT Encode"),
    INVALID_JWT_PARSE(401, "Invalid JWT Parse"),
    INVALID_JWT_SIGN(401, "Invalid JWT Sign"),
    INVALID_JWT_VERIFY(401, "Invalid JWT Verify"),
    INVALID_JWT_ALGORITHM(401, "Invalid JWT Algorithm"),
    INVALID_JWT_KEY(401, "Invalid JWT Key"),
    INVALID_JWT_ENCRYPT(401, "Invalid JWT Encrypt"),
    NOT_FOUND(404, "Not Found"),
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
