package org.personal.shared.exception;

import feign.RetryableException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.status;


/**
 * <p> Responsible for handling and logging all Exceptions thrown in the entire application.
 * <p> Usage:
 * <p> 1. Define a key in the enum {@link org.personal.shared.exception.ErrorCode}
 * <p> 2. Define the key and the value in all files messages_**locale**.properties
 * <p> 3. Run ErrorCodeMessagesTest to check if all keys are present in the files messages_*locale*.properties
 * <p> 4. Throw new {@link org.personal.shared.exception.CustomException} wherever needed
 * <p> If the key is not found in MessageSource, a default message is sent: Internal Translation Error
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
class GlobalExceptionHandler {
	private final MessageSource messageSource;

	@ExceptionHandler(Exception.class)
	ProblemDetail onAnyException(Exception e, HttpServletRequest request) throws Exception {
		if (e instanceof NoResourceFoundException) {
			throw e; // if you don't want to handle certain exceptions (security, ...), re-throw
		}

		String userMessage = messageSource.getMessage(
				ErrorCode.GENERAL.name(),
				null,
				"Internal Translation Error",
				request.getLocale()
		);

		log.error("Unexpected {}: {}", ErrorCode.GENERAL, userMessage, e);
		ProblemDetail problemDetail = ProblemDetail.forStatus(INTERNAL_SERVER_ERROR);
		problemDetail.setDetail(userMessage);
		return problemDetail;
	}

	@ExceptionHandler(CustomException.class)
	ProblemDetail onCustomException(CustomException e, HttpServletRequest request) {
		String userMessage = messageSource.getMessage(
				e.getErrorCode().name(),
				e.getParameters(),
				"Internal Translation Error",
				request.getLocale()
		);
		log.error("CustomException {}: {}", e.getErrorCode(), userMessage, e);

		ProblemDetail problemDetail = ProblemDetail.forStatus(e.getErrorCode().statusCode);
		problemDetail.setDetail(userMessage);
		return problemDetail;
	}

	@ExceptionHandler(BindException.class)
	ProblemDetail onValidationException(BindException e) {
		// Field errors: violations tied to a single property (e.g. @NotBlank on a field)
		List<ValidationError> errors = e.getBindingResult().getFieldErrors().stream()
				.map(this::toValidationError)
				.toList();

		// Global errors: violations tied to the object as a whole (e.g. class-level
		// cross-field validators like @PasswordsMatch), no single field to blame
		List<ValidationError> globalErrors = e.getBindingResult().getGlobalErrors().stream()
				.map(this::toValidationError)
				.toList();

		List<ValidationError> allErrors = List.copyOf(
				Stream.concat(
						errors.stream(),
						globalErrors.stream()
				).toList()
		);

		log.error("Object validation failed: {}", allErrors, e);

		ProblemDetail problemDetail = ProblemDetail.forStatus(BAD_REQUEST);
		problemDetail.setTitle("Your request is not valid.");
		problemDetail.setDetail("Your request is not valid — check the highlighted fields.");
		problemDetail.setProperty("errors", allErrors);
		return problemDetail;
	}

	@ExceptionHandler(HandlerMethodValidationException.class)
	ProblemDetail onMethodParameterValidationException(HandlerMethodValidationException e) {
		List<ValidationError> validationErrors = e.getAllValidationResults().stream()
				.flatMap(result -> result.getResolvableErrors().stream()
						.map(error -> new ValidationError(
										error.getDefaultMessage(),
										toJsonPointer(result.getMethodParameter().getParameterName())
								)
						)
				)
				.toList();

		log.error("Method parameter validation failed: {}", validationErrors, e);

		ProblemDetail problemDetail = ProblemDetail.forStatus(BAD_REQUEST);
		problemDetail.setTitle("Your request is not valid.");
		problemDetail.setDetail("Your request is not valid — check the highlighted fields.");
		problemDetail.setProperty("errors", validationErrors);
		return problemDetail;
	}

// ************************************************************
// *** OPENFEIGN EXCEPTIONS - START
// ************************************************************

	@ExceptionHandler(FeignPropagatedException.class)
	ResponseEntity<String> handleFeignPropagatedException(FeignPropagatedException e) {
		log.error("Feign propagated exception: ", e);
		return status(e.getStatusCode())
				.body(e.getMessage());
	}

	// TODO: This is a workaround, find an improvement.
	// Used to send exceptions about not being able to connect to other backends via open feign
	@ExceptionHandler(RetryableException.class)
	ResponseEntity<String> handleRetryableException(RetryableException e, HttpServletRequest request) {
		String userMessage = messageSource.getMessage(
				ErrorCode.GENERAL.name(),
				null,
				"Internal Translation Error",
				request.getLocale());

		if (e.toString().contains("/products")) {
			log.error("Feign connection error for product backend: ", e);
		}

		return status(SERVICE_UNAVAILABLE)
				.body(userMessage);
	}

// ************************************************************
// *** OPENFEIGN EXCEPTIONS - STOP
// ************************************************************

	private ValidationError toValidationError(FieldError fieldError) {
		return new ValidationError(fieldError.getDefaultMessage(), toJsonPointer(fieldError.getField()));
	}

	private ValidationError toValidationError(ObjectError objectError) {
		return new ValidationError(objectError.getDefaultMessage(), "#");
	}

	/**
	 * Converts a Spring field path into a JSON Pointer (RFC 6901) fragment,
	 * so clients can locate the offending value directly in the request body.
	 *
	 * <p>Input → Output:
	 * <pre>
	 *   "address.street"   → "#/address/street"
	 *   "items[2].name"     → "#/items/2/name"
	 *   "items[0].tags[1]"  → "#/items/0/tags/1"
	 *   null / ""           → "#" (whole document — used for global/object errors)
	 * </pre>
	 */
	private String toJsonPointer(String fieldPath) {
		if (fieldPath == null || fieldPath.isBlank()) {
			return "#";
		}
		String pointer = fieldPath
				.replaceAll("\\[(\\d+)]", "/$1")
				.replace('.', '/');
		return "#/" + pointer;
	}
}
