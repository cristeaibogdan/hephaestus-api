package org.personal.washingmachine.service.microservice;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.shared.exception.FeignPropagatedException;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class CustomErrorDecoder implements ErrorDecoder {

	private final ErrorDecoder defaultErrorDecoder = new Default();

	@Override
	public Exception decode(String methodKey, Response response) {

//**********************************************************
//****** DO NOT USE SOUT, LOGGER OR IDE IN DEBUG AFTER response.body()
//****** An InputStream is only read once, then it is closed
//****** RESULTING IN IOEXCEPTION stream closed.
//**********************************************************

		String requestUrl = response.request().url();
		Response.Body responseBody = response.body();
		HttpStatus responseStatus = HttpStatus.valueOf(response.status());

		// System.out.println("Request URL is = " + requestUrl);
		// System.out.println("Request body is = " + responseBody);
		// System.out.println("Request status is = " + responseStatus);

//**********************************************************
// HANDLE CUSTOM EXCEPTIONS THROWN BY THE CLIENT BACKEND
//**********************************************************
		if (responseStatus == HttpStatus.valueOf(418)) {
			try {
				// 1. Extract the String from the response
				String userMessage = new String(responseBody.asInputStream().readAllBytes());

				// 2. Re-Throw the errorMessage received from the other backend
				return new FeignPropagatedException(userMessage);

			} catch (IOException e) {
				return new CustomException("Error while decoding open feign response", e, ErrorCode.GENERAL);
			}
		}

        /*
        **** HANDLE UNCAUGHT EXCEPTIONS THROWN BY THE BACKEND USING THE FEIGN CLIENT 4xx ****
        Examples:
        1. Method not allowed (e.g., using @GetMapping in Feign client while backend expects @PostMapping).
        2. Not Found (e.g., setting wrong endpoints in Feign client interface).

        **** HANDLE UNCAUGHT EXCEPTIONS THROWN BY THE SERVER BACKEND 5xx ****
        Examples: IllegalArgumentException, NullPointerException, etc.

        Note: OpenFeign's FeignException doesn't bind to a specific HTTP status (i.e., doesn't use Spring's @ResponseStatus annotation),
        which makes Spring default to 500 whenever faced with a FeignException.

        Delegate the other error types to the default error decoder.
        These thrown exceptions will be handled by handleException, in the Global Exception Handler.
        */
		return defaultErrorDecoder.decode(methodKey, response);
	}
}
