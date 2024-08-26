package com.learn.restapiwithboot.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.learn.restapiwithboot.core.dto.BasicResponse;
import com.learn.restapiwithboot.core.exceptions.enums.ErrorType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class FailResponse<T> extends BasicResponse {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private T errs;

    public FailResponse(Integer statusCode, String exceptionMessage) {
        super(statusCode, exceptionMessage);
    }

    @Builder
    public FailResponse(Integer statusCode, String message, T errs) {
        super(statusCode, message);
        this.errs = errs;
    }

    public static <T> ResponseEntity<FailResponse<Object>> ofExceptionResponse(ErrorType errorType, T errs) {
        FailResponse<Object> body = FailResponse.builder()
                .statusCode(errorType.getStatus().value())
                .message(errorType.getMessage())
                .errs(errs)
                .build();
        return ResponseEntity.status(errorType.getStatus()).body(body);
    }
}
