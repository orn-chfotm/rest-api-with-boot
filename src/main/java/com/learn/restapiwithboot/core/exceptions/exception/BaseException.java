package com.learn.restapiwithboot.core.exceptions.exception;

import com.learn.restapiwithboot.core.exceptions.enums.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public abstract class BaseException extends RuntimeException {

    private final ErrorType errorType;

    protected ErrorType getErrorType() {
        return this.errorType;
    }

    /**
     * 하위 클래스에서 이 메서드를 구현할 때는 매개변수로 전달하는 ErrorType 하위 구현체의
     * HTTP 상태 코드를 반환하도록 합니다.
     *
     * @return HttpStatus -> 특정 오류에 대한 HTTP 상태코드
     */
    abstract public HttpStatus getStatus();

    /**
     * 하위 클래스에서 이 메서드를 구현할 때는 매개변수로 전달하는 ErrorType 하위 구현체의
     * 특정 오류에 대한 메시지를 반환하도록 합니다.
     *
     * @return String -> 특정 오류에 대한 메시지
     */
    abstract public String getMessage();

}
