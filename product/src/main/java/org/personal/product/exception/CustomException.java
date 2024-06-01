package org.personal.product.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final Integer errorCode;

    public CustomException(Integer errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
}
