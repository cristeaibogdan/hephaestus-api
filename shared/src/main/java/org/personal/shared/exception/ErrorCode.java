package org.personal.shared.exception;

/**
 * <p> Enum responsible for holding keys that are used in {@link org.personal.shared.exception.GlobalExceptionHandler} to retrieve translated user messages.
 * <p> Keys should be descriptive and illustrate why the exception was thrown in the first place.
 * <p> By default the status code is set to 500, but can be modified for each key.
 */
public enum ErrorCode {

    // TODO: Consider adding HttpStatus instead of int

    // DEFAULT ERROR
    GENERAL,

    // AUTHENTICATION ERRORS
    INVALID_REGISTRATION_CODE(400),
    EMAIL_ALREADY_TAKEN(400),
    USERNAME_ALREADY_TAKEN(400),
    INVALID_CREDENTIALS(400),

    // WASHING-MACHINE ERRORS
    SERIAL_NUMBER_ALREADY_TAKEN(400),
    EMPTY_PAGE(400),
    SERIAL_NUMBER_NOT_FOUND(400),
    REPORT_GENERATION_FAIL,
    INVALID_DATE (400),

    // PRODUCT ERRORS
    NO_MANUFACTURERS_FOUND(400),
    NO_MODELS_TYPES_FOUND_FOR_MANUFACTURER(400);

    public final int statusCode;

    ErrorCode() {
        this(500);
    }

    ErrorCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
