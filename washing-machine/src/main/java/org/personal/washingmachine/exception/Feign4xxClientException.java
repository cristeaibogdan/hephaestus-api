package org.personal.washingmachine.exception;

import lombok.Getter;

@Getter
public class Feign4xxClientException extends RuntimeException {

    private final Integer errorCode;

    public Feign4xxClientException(Integer errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
}
