package org.personal.washingmachine.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorResponse errorResponse;

    public CustomException(Integer errorCode, String errorMessage) {
        this.errorResponse = new ErrorResponse(errorCode, errorMessage);
    }

    public CustomException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }
}
