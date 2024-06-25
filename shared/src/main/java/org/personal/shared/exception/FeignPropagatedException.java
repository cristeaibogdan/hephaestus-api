package org.personal.shared.exception;

import lombok.Getter;

@Getter
public class FeignPropagatedException extends RuntimeException {

    private final Integer errorCode;

    public FeignPropagatedException(Integer errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
}
