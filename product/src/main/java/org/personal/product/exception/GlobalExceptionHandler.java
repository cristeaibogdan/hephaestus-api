package org.personal.product.exception;

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
        log.error("Exception thrown in microservice: {} with the message: {}",
                microserviceName,
                e.getMessage());

        //TODO: Maybe i should remove ErrorResponse since the frontend only needs a code.
        return new ErrorResponse(e.getErrorCode());
    }
}
