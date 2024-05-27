package org.personal.product.exception;

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
}
