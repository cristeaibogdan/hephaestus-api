package org.personal.shared.exception;

import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.I_AM_A_TEAPOT) // CODE 418 = I AM A TEAPOT used to distinguish between custom and general exceptions in frontend
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
	String handleFeignPropagatedException(FeignPropagatedException e, HttpServletRequest request) {
		String userMessage = messageSource.getMessage(
				ErrorCode.GENERAL.name(),
				null,
				request.getLocale());
		log.error("Feign propagated exception: " + userMessage, e);
		return userMessage;
	}

	// TODO: This is a workaround, find an improvement
	// Used to send exceptions about not being able to connect to other backends
	@ExceptionHandler(RetryableException.class)
	String handleRetryableException(RetryableException e, HttpServletRequest request) {
		String userMessage = messageSource.getMessage(
				ErrorCode.GENERAL.name(),
				null,
				request.getLocale());
		log.error("Feign connection error: ", e);

		if (e.toString().contains("/products")) {

		}

		return userMessage;
	}
}
