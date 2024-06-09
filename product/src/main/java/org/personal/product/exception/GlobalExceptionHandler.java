package org.personal.product.exception;

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
    ErrorResponse handleException(Exception e) {
        log.error("Unexpected: " + e);
        return new ErrorResponse(9999);
    }

    @ExceptionHandler(CustomException.class)
    ErrorResponse handleCustomException(CustomException e) {
        log.error("Exception thrown in microservice: {} " + e, microserviceName);

        // CODE 418 = I AM A TEAPOT used to distinguish between custom and general exceptions in frontend

        //TODO: Maybe i should remove ErrorResponse since the frontend only needs a code.
        return new ErrorResponse(e.getErrorCode());
    }
}
