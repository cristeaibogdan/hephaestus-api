package org.personal.shared.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Object[] params;

    public CustomException(ErrorCode errorCode, Object... params) {
        this.errorCode = errorCode;
        this.params = params;
    }

    public CustomException(String message, ErrorCode errorCode,  Object... params) {
        super(message);
        this.errorCode = errorCode;
        this.params = params;
    }

    public CustomException(Throwable cause, ErrorCode errorCode, Object... params) {
        super(cause);
        this.errorCode = errorCode;
        this.params = params;
    }

    public CustomException(String message, Throwable cause, ErrorCode errorCode, Object... params) {
        super(message, cause);
        this.errorCode = errorCode;
        this.params = params;
    }


}
