package org.personal.washingmachine.exception;

import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
public class GlobalExceptionHandler {

    @Value("${spring.application.name}")
    private String microserviceName;

    @ExceptionHandler(Exception.class)
    ErrorResponse handleException(Exception e) { // This is the last safety net!
        log.error("Unexpected: " + e);
        return new ErrorResponse(9999);
    }

    @ExceptionHandler(CustomException.class)
    ErrorResponse handleCustomException(CustomException e) {
        log.error("Exception thrown in microservice: {} " + e, microserviceName);

        // CODE 418 = I AM A TEAPOT is not used in programming,
        // I can use it to distinguish between custom and general exceptions in frontend

        //TODO: Maybe i should remove ErrorResponse since the frontend only needs a code.
        return new ErrorResponse(e.getErrorCode());
    }

// ************************************************************
// *** OPENFEIGN EXCEPTIONS
// ************************************************************

    @ExceptionHandler(FeignPropagatedException.class)
    ErrorResponse handleFeignPropagatedException(FeignPropagatedException e) {
        log.error("Feign propagated exception "+ e);
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
