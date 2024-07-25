package org.personal.shared.exception;

import lombok.Getter;

/**
 * <p> This class is used to propagate the user message from one backend to another.
 * <p><strong>Warning</strong>: Should only be used inside CustomErrorDecoder, and only for backends that are under your control.
 */

@Getter
public class FeignPropagatedException extends RuntimeException {
    public FeignPropagatedException(String userMessage) {
        super(userMessage);
    }
}
