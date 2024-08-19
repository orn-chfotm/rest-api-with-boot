package com.learn.restapiwithboot.core.exceptions.enums;

import com.learn.restapiwithboot.core.dto.response.FailResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface ErrorType {

    HttpStatus getStatus();
    String getMessage();

    default ResponseEntity<FailResponse<Void>> getResponse() {
        return ResponseEntity.status(getStatus()).body(new FailResponse<>(getStatus().value(), getMessage()));
    }
}
