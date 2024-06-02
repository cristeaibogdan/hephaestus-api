package org.personal.washingmachine.service.microservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.personal.washingmachine.exception.*;
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
                ObjectMapper objectMapper = new ObjectMapper();

                // 1. Use Jackson ObjectMapper to extract the custom errorResponse received from the backend
                ErrorResponse errorResponse = objectMapper.readValue(responseBody.asInputStream(), ErrorResponse.class);

                // Comment above line, and uncomment below 2 lines to see what's in the input stream
                // String text = new String(responseBody.asInputStream().readAllBytes());
                // System.out.println("Request body is = " + text);

                // System.out.println("Transformed result = " + errorResponse);

                // 2. Throw the custom errorResponse received from the other backend
                return new FeignPropagatedException(errorResponse.getErrorCode(), "Handled via custom decoder");

            } catch (IOException e) {
                return new CustomException(e, ErrorCode.F_0003, "Error while reading error response in feign CustomErrorDecoder");
            }
        }

        /*
        **** HANDLE UNCAUGHT EXCEPTIONS THROWN BY THE BACKEND USING THE FEIGN CLIENT ****
        Examples:
        1. Method not allowed (e.g., using @GetMapping in Feign client while backend expects @PostMapping).
        2. Not Found (e.g., setting wrong endpoints in Feign client interface).

        **** HANDLE UNCAUGHT EXCEPTIONS THROWN BY THE SERVER BACKEND ****
        Examples: IllegalArgumentException, NullPointerException, etc.

        Note: OpenFeign's FeignException doesn't bind to a specific HTTP status (i.e., doesn't use Spring's @ResponseStatus annotation),
        which makes Spring default to 500 whenever faced with a FeignException.

        Delegate the other error types to the default error decoder.
        These thrown exceptions will be handled by handleException, in the Global Exception Handler.
        */
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
