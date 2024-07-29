package com.learn.restapiwithboot.core.handler;

import com.learn.restapiwithboot.core.dto.response.FailResponse;
import com.learn.restapiwithboot.core.exceptions.BaseException;
import com.learn.restapiwithboot.core.exceptions.impl.*;
import com.learn.restapiwithboot.core.exceptions.enums.ErrorMessage;
import io.jsonwebtoken.JwtException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

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
        ErrorMessage invalidJwtDecode = ErrorMessage.NOT_FOUND;
        return ResponseEntity.status(status).body(new FailResponse<>(
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
        ErrorMessage notFound = ErrorMessage.NOT_FOUND;
        return ResponseEntity.status(status).body(new FailResponse<>(
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
    @ExceptionHandler({JwtException.class})
    protected ResponseEntity<FailResponse> hadelJwtException(JwtException exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorMessage invalidJwtDecode = ErrorMessage.INVALID_JWT_TOKEN;
        return ResponseEntity.status(status).body(new FailResponse<>(
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
        ErrorMessage invalidInputValue = ErrorMessage.INVALID_INPUT_VALUE;
        List<ValidationErrorResponse> notValidList = new ArrayList<>();
        for (FieldError fieldError : exception.getFieldErrors()) {
            notValidList.add(ValidationErrorResponse.builder()
                            .field(fieldError.getField())
                            .message(fieldError.getDefaultMessage())
                    .build()
            );
        }

        return ResponseEntity.badRequest().body(new FailResponse(
                invalidInputValue.getStatus(),
                invalidInputValue.getMessage(),
                notValidList
        ));
    }

    /**
     * '@Valuid' 예외 처리시 에러 메시지를 담을 Response 객체
     * Inner Class
     */
    @Getter
    @Setter
    public static class ValidationErrorResponse {
        private String field;
        private String message;

        @Builder
        public ValidationErrorResponse(String field, String message) {
            this.field = field;
            this.message = message;
        }
    }

    /**
     * ResourceNotFoundException 예외 처리
     *  <p>
     *      JPA find 시 조회 기준에 맞는 결과 값이 없을 때 발생하는 예외 처리
     *  </p>
     */
    @ExceptionHandler({ResourceNotFoundException.class})
    protected ResponseEntity<FailResponse> hadleResourceNotFoundException(ResourceNotFoundException exception) {
        return ResponseEntity.badRequest().body(new FailResponse<>(exception.getStatus(), exception.getMessage()));
    }

    /**
     * TokenInvalidException 예외 처리
     * <p>
     *     JWT 토큰이 유효하지 않을 때 발생하는 예외 처리
     * </p>
     */
    @ExceptionHandler({TokenInvalidException.class})
    protected ResponseEntity<FailResponse> handleInvalidTokenException(TokenInvalidException exception) {
        return ResponseEntity.badRequest().body(new FailResponse<>(exception.getStatus(), exception.getMessage()));
    }

    /**
     * BadCredentialsException 예외 처리
     * <p>
     *     인증, 인가 과정 시 발생하는 예외 처리
     * </p>
     */
    @ExceptionHandler({BadCredentialsException.class})
    protected ResponseEntity<FailResponse> handleBadCredentialsException(BadCredentialsException exception) {
        return ResponseEntity.badRequest().body(new FailResponse<>(exception.getStatus(), exception.getMessage()));
    }

    /**
     * AccountExistenceException 예외 처리
     * <p>
     *     계정 생성 시 이미 존재하는 계정, 탈퇴한 계정일 때 발생하는 예외 처리
     * </p>
     */
    @ExceptionHandler({AccountExistenceException.class})
    protected ResponseEntity<FailResponse> handleAccountExistenceException(AccountExistenceException exception) {
        return ResponseEntity.badRequest().body(new FailResponse<>(exception.getStatus(), exception.getMessage()));
    }

    /* 추후 제거 예정 */
    @Deprecated
    private ResponseEntity<FailResponse> getFailResponseResponseEntity(BaseException exception, ErrorMessage errorMessage) {
        return ResponseEntity.badRequest().body(new FailResponse(
                exception.getStatus() == null ? errorMessage.getStatus() : exception.getStatus(),
                exception.getMessage() == null ? errorMessage.getMessage() : exception.getMessage()
        ));
    }
}
