package org.personal.washingmachine.service.microservice;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;

@Configuration
@RequiredArgsConstructor
public class FeignClientInterceptor implements RequestInterceptor {

	private final HttpServletRequest httpServletRequest;

	@Override
	public void apply(RequestTemplate requestTemplate) {
		String acceptLanguage = httpServletRequest.getHeader("Accept-Language");
		if (acceptLanguage != null) {
			requestTemplate.header("Accept-Language", acceptLanguage);
		}
	}
}
