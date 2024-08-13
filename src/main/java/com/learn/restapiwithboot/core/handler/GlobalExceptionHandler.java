package com.learn.restapiwithboot.core.handler;

import com.learn.restapiwithboot.core.dto.response.FailResponse;
import com.learn.restapiwithboot.core.exceptions.enums.ErrorType;
import com.learn.restapiwithboot.core.exceptions.enums.impl.CommonErrorType;
import com.learn.restapiwithboot.core.exceptions.enums.impl.CredentialsErrorType;
import com.learn.restapiwithboot.core.exceptions.exception.BaseException;
import com.learn.restapiwithboot.core.exceptions.exception.impl.BasicException;
import io.jsonwebtoken.JwtException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 최상위 Exception 처리
     */
    @ExceptionHandler({Throwable.class})
    protected ResponseEntity<FailResponse<Void>> hadleThrowable(Throwable exception) {
        ErrorType errorType = CommonErrorType.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorType.getStatus()).body(new FailResponse<>(
                errorType.getStatus().value(),
                errorType.getMessage()
        ));
    }

    /**
     * Exception 예외 처리
     * <p>
     *      Root Exception 예외 처리로 Exception을 처리하면 모든 예외를 처리할 수 있다.
     * </p>
     */
    @ExceptionHandler({Exception.class})
    protected ResponseEntity<FailResponse<Void>> hadleException(Exception exception) {
        ErrorType errorType = CommonErrorType.BAD_REQEUST;
        return ResponseEntity.status(errorType.getStatus()).body(new FailResponse<>(
                errorType.getStatus().value(),
                errorType.getMessage()
        ));
    }

    /**
     * RuntimeException 예외 처리
     * <p>
     *      Root RuntimeException 예외 처리로 Exception을 처리하면 모든 예외를 처리할 수 있다.
     * </p>
     */
    @ExceptionHandler({RuntimeException.class})
    protected ResponseEntity<FailResponse<Void>> handleRuntimeException(RuntimeException exception) {
        ErrorType errorType = CommonErrorType.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorType.getStatus()).body(new FailResponse<>(
                errorType.getStatus().value(),
                errorType.getMessage()
        ));
    }

    /**
     * JwtException 예외 처리
     * <p>
     *      Root JwtException 예외 처리로 Exception을 처리하면 Jwt 관련 예외를 처리할 수 있다.
     * </p>
     */
    @ExceptionHandler({JwtException.class})
    protected ResponseEntity<FailResponse<Void>> hadleJwtException(JwtException exception) {
        ErrorType errorType = CredentialsErrorType.INVALID_JWT_TOKEN;
        return ResponseEntity.status(errorType.getStatus()).body(new FailResponse<>(
                errorType.getStatus().value(),
                errorType.getMessage()
        ));
    }

    /**
     *  '@Valuid' 예외 처리
     *  <p>
     *      message와 field를 추출하여 반환한다.
     *  </p>
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<FailResponse<List<ValidationErrorResponse>>> hadleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ErrorType errorType = CredentialsErrorType.INVALID_INPUT_VALUE;
        List<ValidationErrorResponse> notValidList = new ArrayList<>();
        for (FieldError fieldError : exception.getFieldErrors()) {
            notValidList.add(ValidationErrorResponse.builder()
                            .field(fieldError.getField())
                            .message(fieldError.getDefaultMessage())
                    .build()
            );
        }

        return ResponseEntity.badRequest().body(new FailResponse<>(
                errorType.getStatus().value(),
                errorType.getMessage(),
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
     *  예외 처리 통합 -> BasicException.class
     */
    @ExceptionHandler({BasicException.class})
    protected ResponseEntity<FailResponse<Void>> hadleBasicException(BasicException exception) {
        return ResponseEntity.badRequest().body(new FailResponse<>(exception.getStatus().value(), exception.getMessage()));
    }
}
