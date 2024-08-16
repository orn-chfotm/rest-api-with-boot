package com.learn.restapiwithboot.core.exceptions.exception;

import com.learn.restapiwithboot.core.exceptions.enums.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {

    private final ErrorType errorType;

    protected BaseException(ErrorType errorType) {
        this.errorType = errorType;
    }

    protected ErrorType getErrorType() {
        return this.errorType;
    }

    /**
     * 하위 클래스에서 이 메서드를 오버라이딩할 때는 매개변수로 전달하는 ErrorType 하위 구현체의
     * HTTP 상태 코드와 같은 형식을 반환하도록 합니다.
     *
     * @return HttpStatus -> 특정 오류에 대한 HTTP 상태코드
     */
    public HttpStatus getStatus() {
        return this.errorType.getStatus();
    }

    /**
     * 하위 클래스에서 이 메서드를 구현할 때는 매개변수로 전달하는 ErrorType 하위 구현체의
     * 특정 오류에 대한 메시지와 같은 형식을 반환하도록 합니다.
     *
     * @return String -> 특정 오류에 대한 메시지
     */
    public String getMessage() {
        return this.errorType.getMessage();
    }

}
