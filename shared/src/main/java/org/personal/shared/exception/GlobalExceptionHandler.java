package org.personal.shared.exception;

import feign.RetryableException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * <p> Responsible for handling and logging all Exceptions thrown in the entire application.
 * <p> The {@link HttpStatus#I_AM_A_TEAPOT} is used to distinguish between custom and general exceptions in the frontend. Subject to change!
 * <p> Usage:
 * <p> 1. Define a key in the enum {@link org.personal.shared.exception.ErrorCode}
 * <p> 2. Define the key and the value in all files messages_**locale**.properties
 * <p> 3. Run ErrorCodeMessagesTest to check if all keys are present in the files messages_*locale*.properties
 * <p> 4. Throw new {@link org.personal.shared.exception.CustomException} wherever needed
 */

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
class GlobalExceptionHandler {
	private final MessageSource messageSource;

	@ExceptionHandler(Exception.class)
	String handleAnyException(Exception e, HttpServletRequest request) {
		String userMessage = messageSource.getMessage(
				ErrorCode.GENERAL.name(),
				null,
				request.getLocale());
		log.error("Unexpected: " + userMessage, e);
		return userMessage;
	}

	@ExceptionHandler(CustomException.class)
	String handleCustomException(CustomException e, HttpServletRequest request) {
		String userMessage = messageSource.getMessage(
				e.getErrorCode().name(),
				e.getParams(),
				request.getLocale());
		log.error("CustomException: " + userMessage, e);
		return userMessage;
	}

// ************************************************************
// *** OPENFEIGN EXCEPTIONS
// ************************************************************

	@ExceptionHandler(FeignPropagatedException.class)
	String handleFeignPropagatedException(FeignPropagatedException e) {
		log.error("Feign propagated exception: ", e);
		return e.getMessage();
	}

	// TODO: This is a workaround, find an improvement
	// Used to send exceptions about not being able to connect to other backends
	@ExceptionHandler(RetryableException.class)
	String handleRetryableException(RetryableException e, HttpServletRequest request) {
		String userMessage = messageSource.getMessage(
				ErrorCode.GENERAL.name(),
				null,
				request.getLocale());

		if (e.toString().contains("/products")) {
			log.error("Feign connection error for product backend: ", e);
		}

		return userMessage;
	}
}
