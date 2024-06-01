package org.personal.washingmachine.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final Integer errorCode;

    public CustomException(Integer errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }

    public CustomException(Throwable cause, Integer errorCode, String errorMessage) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
    }
}
