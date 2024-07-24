package org.personal.shared.exception;

import lombok.Getter;

@Getter
public class FeignPropagatedException extends RuntimeException {
    public FeignPropagatedException(String userMessage) {
        super(userMessage);
    }
}
