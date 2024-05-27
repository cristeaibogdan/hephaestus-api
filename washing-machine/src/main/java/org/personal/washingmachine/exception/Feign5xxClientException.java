package org.personal.washingmachine.exception;

public class Feign5xxClientException extends RuntimeException {

    private final ErrorResponse errorResponse;

    public Feign5xxClientException(Integer errorCode, String errorMessage) {
        this.errorResponse = new ErrorResponse(errorCode, errorMessage);
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
