package com.learn.restapiwithboot.core.handler;

import com.learn.restapiwithboot.core.dto.response.FailResponse;
import com.learn.restapiwithboot.core.exceptions.enums.impl.CommonErrorType;
import com.learn.restapiwithboot.core.exceptions.enums.impl.CredentialsErrorType;
import com.learn.restapiwithboot.core.exceptions.exception.ext.BasicException;
import com.learn.restapiwithboot.core.handler.response.HandlerResponse;
import io.jsonwebtoken.JwtException;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final HandlerResponse handlerResponse;

    /**
     * 최상위 Exception 처리
     */
    @ExceptionHandler({Throwable.class})
    protected ResponseEntity<FailResponse<Object>> handleThrowable(Throwable e) {
        log.error("Handle [{}] with a message ::", e.getClass().getName() ,e);
        return FailResponse.ofExceptionResponse(CommonErrorType.INTERNAL_SERVER_ERROR, null);
    }

    /**
     * Runtime Exception 예외 처리
     * <p>
     *      UnChecked Exception
     *      커스텀 Exception에서 처리되지 못한 예외 공통 처리
     * </p>
     */
    @ExceptionHandler({RuntimeException.class})
    protected ResponseEntity<FailResponse<Object>> handleException(RuntimeException e) {
        log.error("Handle [{}] with a message ::", e.getClass().getName() ,e);
        if(e instanceof BasicException) {
            return FailResponse.ofExceptionResponse(((BasicException) e).getErrorType(), null);
        }
        return FailResponse.ofExceptionResponse(CommonErrorType.BAD_REQEUST, null);
    }

    /**
     * JwtException 예외 처리
     * <p>
     *      Root JwtException 예외 처리로 Exception을 처리하면 Jwt 관련 예외를 처리할 수 있다.
     * </p>
     */
    @ExceptionHandler({JwtException.class})
    protected ResponseEntity<FailResponse<Object>> handleJwtException(JwtException e) {
        log.error("Handle [{}] with a message ::", e.getClass().getName() ,e);
        return FailResponse.ofExceptionResponse(CredentialsErrorType.INVALID_JWT_TOKEN, null);
    }

    /**
     *  '@Valuid' 예외 처리
     *  <p>
     *      message와 field를 추출하여 반환한다.
     *  </p>
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<FailResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Handle [{}] with a message ::", e.getClass().getName() ,e);
        List<ValidationErrorResponse> notValidList = new ArrayList<>();
        for (FieldError fieldError : e.getFieldErrors()) {
            notValidList.add(ValidationErrorResponse.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build()
            );
        }
        return FailResponse.ofExceptionResponse(CredentialsErrorType.INVALID_INPUT_VALUE, notValidList);
    }

    /**
     * '@Valuid' 예외 처리시 에러 메시지를 담을 Response 객체
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

}
