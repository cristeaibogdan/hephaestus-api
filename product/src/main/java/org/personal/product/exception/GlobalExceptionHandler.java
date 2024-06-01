package org.personal.product.exception;

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
        log.error("Exception thrown in microservice: {} with the message: {}",
                microserviceName,
                e.getMessage());

        //TODO: Maybe i should remove ErrorResponse since the frontend only needs a code.
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode());

        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(errorResponse);
    }
}
