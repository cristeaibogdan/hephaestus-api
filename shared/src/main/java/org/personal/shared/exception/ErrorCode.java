package org.personal.shared.exception;

import org.springframework.http.HttpStatus;

/**
 * <p> Enum responsible for holding keys that are used in {@link org.personal.shared.exception.GlobalExceptionHandler} to retrieve translated user messages.
 * <p> Keys should be descriptive and illustrate why the exception was thrown in the first place.
 * <p> By default the status code is set to {@link HttpStatus#INTERNAL_SERVER_ERROR INTERNAL_SERVER_ERROR}, but can be modified for each key.
 */
public enum ErrorCode {

    // DEFAULT ERROR
    GENERAL (HttpStatus.INTERNAL_SERVER_ERROR),

    // AUTHENTICATION ERRORS
    INVALID_REGISTRATION_CODE (HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_TAKEN (HttpStatus.CONFLICT),
    USERNAME_ALREADY_TAKEN (HttpStatus.CONFLICT),
    INVALID_CREDENTIALS (HttpStatus.UNAUTHORIZED),

    // WASHING-MACHINE ERRORS
    SERIAL_NUMBER_ALREADY_TAKEN (HttpStatus.CONFLICT),
    SERIAL_NUMBER_NOT_FOUND (HttpStatus.NOT_FOUND),
    REPORT_GENERATION_FAIL (HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_DATE (HttpStatus.BAD_REQUEST),

    // MANUAL VALIDATION ERRORS
    SERIAL_NUMBERS_NOT_FOUND (HttpStatus.NOT_FOUND),

    // PRODUCT ERRORS
    NO_MANUFACTURERS_FOUND (HttpStatus.NOT_FOUND),
    NO_MODELS_TYPES_FOUND_FOR_MANUFACTURER (HttpStatus.NOT_FOUND),
    QR_CODE_NOT_FOUND (HttpStatus.NOT_FOUND);

    public final HttpStatus statusCode;

    ErrorCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }
}
