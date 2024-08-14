package org.personal.shared.exception;

import feign.RetryableException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.ResponseEntity.status;


/**
 * <p> Responsible for handling and logging all Exceptions thrown in the entire application.
 * <p> Usage:
 * <p> 1. Define a key in the enum {@link org.personal.shared.exception.ErrorCode}
 * <p> 2. Define the key and the value in all files messages_**locale**.properties
 * <p> 3. Run ErrorCodeMessagesTest to check if all keys are present in the files messages_*locale*.properties
 * <p> 4. Throw new {@link org.personal.shared.exception.CustomException} wherever needed
 */

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
class GlobalExceptionHandler {
	private final MessageSource messageSource;

	@ExceptionHandler(Exception.class)
	ResponseEntity<String> handleAnyException(Exception e, HttpServletRequest request) {
		String userMessage = messageSource.getMessage(
				ErrorCode.GENERAL.name(),
				null,
				request.getLocale());
		log.error("Unexpected {}: {}", ErrorCode.GENERAL, userMessage, e);
		return status(500).body(userMessage);
	}

	@ExceptionHandler(CustomException.class)
	ResponseEntity<String> handleCustomException(CustomException e, HttpServletRequest request) {
		String userMessage = messageSource.getMessage(
				e.getErrorCode().name(),
				e.getParams(),
				request.getLocale());
		log.error("CustomException {}: {}", e.getErrorCode(), userMessage, e);
		return status(e.getErrorCode().statusCode).body(userMessage);
	}

// ************************************************************
// *** OPENFEIGN EXCEPTIONS
// ************************************************************

	@ExceptionHandler(FeignPropagatedException.class)
	ResponseEntity<String> handleFeignPropagatedException(FeignPropagatedException e) {
		log.error("Feign propagated exception: ", e);
		return status(e.getStatusCode()).body(e.getMessage());
	}

	// TODO: This is a workaround, find an improvement.
	// Used to send exceptions about not being able to connect to other backends via open feign
	@ExceptionHandler(RetryableException.class)
	ResponseEntity<String> handleRetryableException(RetryableException e, HttpServletRequest request) {
		String userMessage = messageSource.getMessage(
				ErrorCode.GENERAL.name(),
				null,
				request.getLocale());

		if (e.toString().contains("/products")) {
			log.error("Feign connection error for product backend: ", e);
		}

		return status(503).body(userMessage);
	}
}
