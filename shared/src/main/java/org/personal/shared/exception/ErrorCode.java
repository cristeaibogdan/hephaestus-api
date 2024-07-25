package org.personal.shared.exception;

/**
 * <p> This class is responsible for holding keys that are used in {@link org.personal.shared.exception.GlobalExceptionHandler} to retrieve translated user messages.
 * <p> Keys should be descriptive and illustrate why the exception was thrown in the first place.
 */

public enum ErrorCode {

    // DEFAULT ERROR
    GENERAL,

    // AUTHENTICATION ERRORS
    INVALID_REGISTRATION_CODE,
    EMAIL_ALREADY_TAKEN,
    USERNAME_ALREADY_TAKEN,
    INVALID_CREDENTIALS,

    // WASHING-MACHINE ERRORS
    SERIAL_NUMBER_ALREADY_TAKEN,
    EMPTY_PAGE,
    SERIAL_NUMBER_NOT_FOUND,
    REPORT_GENERATION_FAIL,

    // PRODUCT ERRORS
    NO_MANUFACTURERS_FOUND,
    NO_MODELS_TYPES_FOUND_FOR_MANUFACTURER
}
