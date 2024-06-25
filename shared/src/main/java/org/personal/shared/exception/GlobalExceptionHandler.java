package org.personal.shared.exception;

import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@ResponseStatus(HttpStatus.I_AM_A_TEAPOT) // CODE 418 = I AM A TEAPOT used to distinguish between custom and general exceptions in frontend
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    ErrorResponse handleException(Exception e) {
        log.error("Unexpected: " + e);
        return new ErrorResponse(ErrorCode.E_9999);
    }

    @ExceptionHandler(CustomException.class)
    ErrorResponse handleCustomException(CustomException e) {
        log.error("CustomException thrown: " + e.getMessage());

        if(e.getCause() != null) {
            log.error("Caused by: " + e.getCause());
        }

        //TODO: Maybe i should remove ErrorResponse since the frontend only needs a code.
        return new ErrorResponse(e.getErrorCode());
    }

// ************************************************************
// *** OPENFEIGN EXCEPTIONS
// ************************************************************

    @ExceptionHandler(FeignPropagatedException.class)
    ErrorResponse handleFeignPropagatedException(FeignPropagatedException e) {
        log.error("Feign propagated exception " + e);
        return new ErrorResponse(e.getErrorCode());
    }

    // Used to send exceptions about not being able to connect to other backends
    @ExceptionHandler(RetryableException.class)
    ErrorResponse handleRetryableException(RetryableException e) {
        log.error("Feign connection error: " + e);

        ErrorResponse errorResponse = new ErrorResponse();

        if (e.toString().contains("/products")) {
            errorResponse.setErrorCode(ErrorCode.E_2000);
        }

        return errorResponse;
    }
}
