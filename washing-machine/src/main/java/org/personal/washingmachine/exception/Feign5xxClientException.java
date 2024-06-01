package org.personal.washingmachine.exception;

import lombok.Getter;

@Getter
public class Feign5xxClientException extends RuntimeException {

    private final Integer errorCode;

    public Feign5xxClientException(Integer errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
}
