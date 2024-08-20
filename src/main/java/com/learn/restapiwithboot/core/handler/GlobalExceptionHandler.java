package com.learn.restapiwithboot.core.handler;

import com.learn.restapiwithboot.core.dto.response.FailResponse;
import com.learn.restapiwithboot.core.exceptions.enums.ErrorType;
import com.learn.restapiwithboot.core.exceptions.enums.impl.CommonErrorType;
import com.learn.restapiwithboot.core.exceptions.enums.impl.CredentialsErrorType;
import com.learn.restapiwithboot.core.exceptions.exception.ext.BasicException;
import com.learn.restapiwithboot.core.handler.response.HandlerResponse;
import com.learn.restapiwithboot.core.handler.response.ValidationErrorResponse;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final HandlerResponse handlerResponse;

    /**
     * 최상위 Exception 처리
     */
    @ExceptionHandler({Throwable.class})
    protected ResponseEntity<FailResponse<Void>> hadleThrowable(Throwable exception) {
        return handlerResponse.getResponse(exception, CommonErrorType.INTERNAL_SERVER_ERROR);
    }

    /**
     * Runtime Exception 예외 처리
     * <p>
     *      UnChecked Exception
     *      커스텀 Exception에서 처리되지 못한 예외 공통 처리
     * </p>
     */
    @ExceptionHandler({RuntimeException.class})
    protected ResponseEntity<FailResponse<Void>> hadleException(RuntimeException exception) {
        return handlerResponse.getResponse(exception, CommonErrorType.BAD_REQEUST);
    }

    /**
     * JwtException 예외 처리
     * <p>
     *      Root JwtException 예외 처리로 Exception을 처리하면 Jwt 관련 예외를 처리할 수 있다.
     * </p>
     */
    @ExceptionHandler({JwtException.class})
    protected ResponseEntity<FailResponse<Void>> hadleJwtException(JwtException exception) {
        return handlerResponse.getResponse(exception, CredentialsErrorType.INVALID_JWT_TOKEN);
    }

    /**
     *  '@Valuid' 예외 처리
     *  <p>
     *      message와 field를 추출하여 반환한다.
     *  </p>
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<FailResponse<List<ValidationErrorResponse>>> hadleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<ValidationErrorResponse> notValidList = new ArrayList<>();
        for (FieldError fieldError : exception.getFieldErrors()) {
            notValidList.add(ValidationErrorResponse.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build()
            );
        }

        return handlerResponse.getNotValidResponse(notValidList, CredentialsErrorType.INVALID_INPUT_VALUE);
    }

    /**
     *  예외 처리 통합 -> BasicException.class
     */
    @ExceptionHandler({BasicException.class})
    protected ResponseEntity<FailResponse<Void>> hadleBasicException(BasicException exception) {
        return handlerResponse.getResponse(exception.getErrorType());
    }
}
