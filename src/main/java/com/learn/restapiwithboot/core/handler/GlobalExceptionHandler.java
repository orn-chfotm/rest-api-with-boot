package com.learn.restapiwithboot.core.handler;

import com.learn.restapiwithboot.common.dto.response.FailResponse;
import com.learn.restapiwithboot.core.enums.Exceptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Exception 예외 처리
     * <p>
     *      Root Exception 예외 처리로 Exception을 처리하면 모든 예외를 처리할 수 있다.
     * </p>
     */
    @ExceptionHandler({Exception.class})
    protected ResponseEntity<FailResponse> hadelException(Exception e) {
        Exceptions invalidJwtDecode = Exceptions.INVALID_JWT_DECODE;
        return ResponseEntity.badRequest().body(new FailResponse(
                invalidJwtDecode.getStatus(),
                e.getMessage(),
                invalidJwtDecode.getMessage()
        ));
    }

    /**
     * RuntimeException 예외 처리
     * <p>
     *      Root RuntimeException 예외 처리로 Exception을 처리하면 모든 예외를 처리할 수 있다.
     * </p>
     */
    @ExceptionHandler({RuntimeException.class})
    protected ResponseEntity<FailResponse> handelRuntimeException(RuntimeException e) {
        Exceptions notFound = Exceptions.NOT_FOUND;
        return ResponseEntity.badRequest().body(new FailResponse(
                notFound.getStatus(),
                e.getMessage(),
                notFound.getMessage()
        ));
    }

}
