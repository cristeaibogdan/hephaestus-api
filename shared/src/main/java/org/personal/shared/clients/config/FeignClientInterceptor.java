package org.personal.shared.clients.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;


/**
 * <p> This class is used to attach headers to a request sent via OpenFein.
 * <p> By default OpenFeign does not propagate the requests' headers, they need to be manually added
 */
@Configuration
@RequiredArgsConstructor
public class FeignClientInterceptor implements RequestInterceptor {

	private final HttpServletRequest httpServletRequest;

	@Override
	public void apply(RequestTemplate requestTemplate) {
		String acceptLanguage = httpServletRequest.getHeader(HttpHeaders.ACCEPT_LANGUAGE);

		if (acceptLanguage != null) {
			requestTemplate.header(HttpHeaders.ACCEPT_LANGUAGE, acceptLanguage);
		}
	}
}
