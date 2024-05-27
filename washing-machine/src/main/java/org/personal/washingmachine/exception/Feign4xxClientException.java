package org.personal.washingmachine.exception;

public class Feign4xxClientException extends RuntimeException {

    private final ErrorResponse errorResponse;

    public Feign4xxClientException(Integer errorCode, String errorMessage) {
        this.errorResponse = new ErrorResponse(errorCode, errorMessage);
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
