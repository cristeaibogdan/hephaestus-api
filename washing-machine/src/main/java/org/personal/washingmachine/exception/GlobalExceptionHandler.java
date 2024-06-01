package org.personal.washingmachine.exception;

import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.application.name}")
    private String microserviceName;

    @ExceptionHandler(CustomException.class)
    ResponseEntity<Object> handleCustomException(CustomException e) {
        log.error("Exception thrown in microservice: {} with the message: {}.",
                microserviceName,
                e.getMessage());
        log.error("Exception : ", e);

        // CODE 418 = I AM A TEAPOT is not used in programming,
        // I can use it to distinguish between custom and general exceptions in frontend

        //TODO: Maybe i should remove ErrorResponse since the frontend only needs a code.
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode());

        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(errorResponse);
    }

// ************************************************************
// *** OPENFEIGN EXCEPTIONS
// ************************************************************

    @ExceptionHandler(FeignPropagatedException.class)
    ResponseEntity<Object> handleFeignPropagatedException(FeignPropagatedException e) {
        log.error("Feign propagated exception with the message: {}",
                e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode());

        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(errorResponse);
    }

    // Used to send exceptions about not being able to connect to other backends
    @ExceptionHandler(RetryableException.class)
    ResponseEntity<Object> handleRetryableException(RetryableException e) {
        log.error("Feign connection error: " + e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse();

        if (e.toString().contains("/products")) {
            errorResponse.setErrorCode(ErrorCode.E_2000);
        }

        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(errorResponse);
    }

    @ExceptionHandler(Feign4xxClientException.class)
    ResponseEntity<Object> handleFeign4xxClientException(Feign4xxClientException e) {
        log.error("Feign 4xxx Exception : {}",
                e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode());

        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(errorResponse);
    }

    @ExceptionHandler(Feign5xxClientException.class)
    ResponseEntity<Object> handleFeign5xxClientException(Feign5xxClientException e) {
        log.error("Feign 5xx Exception : {}",
                e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode());

        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(errorResponse);
    }
}
