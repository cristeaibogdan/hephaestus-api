package org.personal.shared.exception;

import org.springframework.http.HttpStatus;

/**
 * <p> Enum responsible for holding keys that are used in {@link org.personal.shared.exception.GlobalExceptionHandler} to retrieve translated user messages.
 * <p> Keys should be descriptive and illustrate why the exception was thrown in the first place.
 * <p> By default the status code is set to {@link HttpStatus#INTERNAL_SERVER_ERROR INTERNAL_SERVER_ERROR}, but can be modified for each key.
 */
public enum ErrorCode {

    // DEFAULT ERROR
    GENERAL,

    NOT_FOUND,

    // AUTHENTICATION ERRORS
    INVALID_REGISTRATION_CODE (HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_TAKEN (HttpStatus.BAD_REQUEST),
    USERNAME_ALREADY_TAKEN (HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS (HttpStatus.BAD_REQUEST),

    // WASHING-MACHINE ERRORS
    SERIAL_NUMBER_ALREADY_TAKEN (HttpStatus.BAD_REQUEST),
    SERIAL_NUMBER_NOT_FOUND (HttpStatus.NOT_FOUND),
    REPORT_GENERATION_FAIL,
    INVALID_DATE (HttpStatus.BAD_REQUEST),

    // MANUAL VALIDATION ERRORS
    LIST_IS_EMPTY(HttpStatus.BAD_REQUEST),
    SERIAL_NUMBERS_NOT_FOUND (HttpStatus.NOT_FOUND),

    // PRODUCT ERRORS
    NO_MANUFACTURERS_FOUND (HttpStatus.BAD_REQUEST),
    NO_MODELS_TYPES_FOUND_FOR_MANUFACTURER (HttpStatus.BAD_REQUEST);

    public final HttpStatus statusCode;

    ErrorCode() {
        this(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    ErrorCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }
}
