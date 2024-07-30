package org.personal.shared.exception;

import lombok.Getter;

/**
 * <p> Custom implementation of RuntimeException used to handle business logic. Use cases:
 * <p> a. Most common scenario
 * <pre>{@code throw new CustomException(ErrorCode.SERIAL_NUMBER_NOT_FOUND)}</pre>
 * <p> b. Scenario where you have to rethrow a checked exception
 * <pre>{@code throw new CustomException(e, ErrorCode.REPORT_GENERATION_FAIL)}</pre>
 * <p> c. Scenario where you provide helpful debug information
 * <pre>{@code
 *     throw new CustomException(
 *     	"Could not extract bytes from image: " + imageFile.getName(),
 *     	e,
 *     	ErrorCode.GENERAL)}
 * </pre>
 * <p> d. Scenario where you send back the information provided by the user
 * <pre>{@code throw new CustomException(ErrorCode.SERIAL_NUMBER_NOT_FOUND, serialNumber)}</pre>
*/

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Object[] params;

    public CustomException(ErrorCode errorCode, Object... params) {
        this.errorCode = errorCode;
        this.params = params;
    }

    public CustomException(String message, ErrorCode errorCode,  Object... params) {
        super(message);
        this.errorCode = errorCode;
        this.params = params;
    }

    public CustomException(Throwable cause, ErrorCode errorCode, Object... params) {
        super(cause);
        this.errorCode = errorCode;
        this.params = params;
    }

    public CustomException(String message, Throwable cause, ErrorCode errorCode, Object... params) {
        super(message, cause);
        this.errorCode = errorCode;
        this.params = params;
    }


}
