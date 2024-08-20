package com.learn.restapiwithboot.core.handler.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * '@Valuid' 예외 처리시 에러 메시지를 담을 Response 객체
 */

@Getter
@Setter
public class ValidationErrorResponse {
    private String field;
    private String message;

    @Builder
    public ValidationErrorResponse(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
