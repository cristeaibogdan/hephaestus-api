package org.personal.shared.clients.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.shared.exception.FeignPropagatedException;

import java.io.IOException;

/**
 * <p> This class is responsible for decoding {@link org.personal.shared.exception.CustomException} thrown by other microservices.
 */
public class CustomErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {

//**********************************************************
//****** DO NOT USE SOUT, LOGGER OR IDE IN DEBUG AFTER response.body()
//****** An InputStream is only read once, then it is closed
//****** RESULTING IN IOEXCEPTION stream closed.
//**********************************************************

		String requestUrl = response.request().url();
		Response.Body responseBody = response.body();

		// System.out.println("Request URL is = " + requestUrl);
		// System.out.println("Request body is = " + responseBody);
		// System.out.println("Request status is = " + response.status());

//**********************************************************
// HANDLE CUSTOM EXCEPTIONS THROWN BY THE CLIENT BACKEND
//**********************************************************
		try {
			// 1. Extract the String from the response
			String userMessage = new String(responseBody.asInputStream().readAllBytes());

			// 2. Re-Throw the errorMessage and the status received from the other backend
			return new FeignPropagatedException(userMessage, response.status());

		} catch (IOException e) {
			return new CustomException("Error while decoding open feign response", e, ErrorCode.GENERAL);
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
        */
	}
}
