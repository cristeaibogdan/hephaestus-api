package org.personal.washingmachine.exception;

import feign.RetryableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    ResponseEntity<Object> handleCustomException (CustomException e) {

        // CODE 418 = I AM A TEAPOT is not used in programming,
        // I can use it to distinguish between custom and general exceptions in frontend

        return new ResponseEntity<>(e.getErrorResponse(), HttpStatus.I_AM_A_TEAPOT);
    }

// ************************************************************
// *** OPENFEIGN EXCEPTIONS
// ************************************************************

    // Used to send exceptions about not being able to connect to other backends
    @ExceptionHandler(RetryableException.class)
    ResponseEntity<Object> handleRetryableException (RetryableException e) {

        System.out.println("Retryable error = "+e.toString());

        ErrorResponse errorResponse = new ErrorResponse();

        if (e.toString().contains("/common-products")) {
            errorResponse.setErrorCode(ErrorCode.E_2000);
            errorResponse.setErrorMessage("Could not establish connection to common server");
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler(Feign4xxClientException.class)
    ResponseEntity<Object> handleFeign4xxClientException (Feign4xxClientException e) {
        return new ResponseEntity<>(e.getErrorResponse(), HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler(Feign5xxClientException.class)
    ResponseEntity<Object> handleFeign5xxClientException (Feign5xxClientException e) {
        return new ResponseEntity<>(e.getErrorResponse(), HttpStatus.I_AM_A_TEAPOT);
    }
}
