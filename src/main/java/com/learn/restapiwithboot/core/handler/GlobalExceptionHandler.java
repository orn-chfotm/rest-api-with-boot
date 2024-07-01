package com.learn.restapiwithboot.core.handler;

import com.learn.restapiwithboot.common.dto.response.FailResponse;
import com.learn.restapiwithboot.core.enums.Exceptions;
import com.learn.restapiwithboot.core.exceptions.TokenInvalidException;
import com.learn.restapiwithboot.core.exceptions.ResourceNotFoundException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Exception 예외 처리
     * <p>
     *      Root Exception 예외 처리로 Exception을 처리하면 모든 예외를 처리할 수 있다.
     * </p>
     */
    @ExceptionHandler({Exception.class})
    protected ResponseEntity<FailResponse> hadelException(Exception exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Exceptions invalidJwtDecode = Exceptions.NOT_FOUND;
        return ResponseEntity.status(status).body(new FailResponse(
                invalidJwtDecode.getStatus(),
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
    protected ResponseEntity<FailResponse> handelRuntimeException(RuntimeException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Exceptions notFound = Exceptions.NOT_FOUND;
        return ResponseEntity.status(status).body(new FailResponse(
                notFound.getStatus(),
                notFound.getMessage()
        ));
    }

    /**
     * JwtException 예외 처리
     * <p>
     *      Root JwtException 예외 처리로 Exception을 처리하면 Jwt 관련 예외를 처리할 수 있다.
     *      Run
     * </p>
     */
    @ExceptionHandler({Exception.class})
    protected ResponseEntity<FailResponse> hadelJwtException(JwtException exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Exceptions invalidJwtDecode = Exceptions.INVALID_JWT_TOKEN;
        return ResponseEntity.status(status).body(new FailResponse(
                invalidJwtDecode.getStatus(),
                invalidJwtDecode.getMessage()
        ));
    }

    /**
     *  '@Valuid' 예외 처리
     *  <p>
     *      message와 field를 추출하여 반환한다.
     *  </p>
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<FailResponse> hadleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Exceptions invalidInputValue = Exceptions.INVALID_INPUT_VALUE;
        Map<String, String> errMessageMap = new HashMap<>();
        for (FieldError fieldError : exception.getFieldErrors()) {
            errMessageMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(new FailResponse(
                invalidInputValue.getStatus(),
                invalidInputValue.getMessage(),
                errMessageMap
        ));
    }

    /**
     * ResourceNotFoundException 예외 처리
     *  <p>
     *      JPA find 시 조회 기준에 맞는 결과 값이 없을 때 발생하는 예외 처리
     *  </p>
     */
    @ExceptionHandler({ResourceNotFoundException.class})
    protected ResponseEntity<FailResponse> hadleResourceNotFoundException(ResourceNotFoundException exception) {
        return ResponseEntity.badRequest().body(new FailResponse(
                exception.getStatus(),
                exception.getDetailMessage()
        ));
    }

    @ExceptionHandler({TokenInvalidException.class})
    protected ResponseEntity<FailResponse> hadleInvalidTokenException(TokenInvalidException exception) {
        return ResponseEntity.badRequest().body(new FailResponse(
                exception.getStatus(),
                exception.getDetailMessage()
        ));
    }

}
