package com.learn.restapiwithboot.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.learn.restapiwithboot.core.dto.BasicResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class SuccessResponse<T> extends BasicResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    public SuccessResponse(HttpStatus statusCode, String message, T data) {
        super(statusCode, message);
        this.data = data;
    }

    public static <T> ResponseEntity<?> of(T data) {
        final HttpStatus success = HttpStatus.OK;
        return new ResponseEntity<>(new SuccessResponse<>(success, success.getReasonPhrase(), data), success);
    }
}
