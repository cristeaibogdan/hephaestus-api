package org.personal.washingmachine.infra;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;

@Configuration
public class SwaggerBeanConfig {

	/*
	Workaround for Swagger UI sending octet-stream content
	https://github.com/swagger-api/swagger-ui/issues/6462
	https://github.com/swagger-api/swagger-ui/issues/4826
	*/
	public SwaggerBeanConfig(MappingJackson2HttpMessageConverter converter) {
		var supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
		supportedMediaTypes.add(new MediaType("application", "octet-stream"));
		converter.setSupportedMediaTypes(supportedMediaTypes);
	}
}
