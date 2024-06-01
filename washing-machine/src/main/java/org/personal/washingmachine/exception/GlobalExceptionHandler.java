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
public class GlobalExceptionHandler {

    @Value("${spring.application.name}")
    private String microserviceName;

    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    ErrorResponse handleCustomException(CustomException e) {
        log.error("Exception thrown in microservice: {} with the message: {}.",
                microserviceName,
                e.getMessage());
        log.error("Exception : ", e);

        // CODE 418 = I AM A TEAPOT is not used in programming,
        // I can use it to distinguish between custom and general exceptions in frontend

        //TODO: Maybe i should remove ErrorResponse since the frontend only needs a code.
        return new ErrorResponse(e.getErrorCode());
    }

// ************************************************************
// *** OPENFEIGN EXCEPTIONS
// ************************************************************

    @ExceptionHandler(FeignPropagatedException.class)
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    ErrorResponse handleFeignPropagatedException(FeignPropagatedException e) {
        log.error("Feign propagated exception with the message: {}",
                e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode());

        return errorResponse;
    }

    // Used to send exceptions about not being able to connect to other backends
    @ExceptionHandler(RetryableException.class)
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    ErrorResponse handleRetryableException(RetryableException e) {
        log.error("Feign connection error: " + e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse();

        if (e.toString().contains("/products")) {
            errorResponse.setErrorCode(ErrorCode.E_2000);
        }

        return errorResponse;
    }

    @ExceptionHandler(Feign4xxClientException.class)
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    ErrorResponse handleFeign4xxClientException(Feign4xxClientException e) {
        log.error("Feign 4xxx Exception : {}",
                e.getMessage());

        return new ErrorResponse(e.getErrorCode());
    }

    @ExceptionHandler(Feign5xxClientException.class)
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    ErrorResponse handleFeign5xxClientException(Feign5xxClientException e) {
        log.error("Feign 5xx Exception : {}",
                e.getMessage());

        return new ErrorResponse(e.getErrorCode());
    }
}
