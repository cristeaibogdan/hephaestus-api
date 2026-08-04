package org.personal.washingmachine.usecase.user.getorganizationandcountry;

import org.personal.washingmachine.usecase.user.RegistrationCodeContainer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
class GetOrganizationAndCountry { // TODO: Replace with proper authentication
	private final RegistrationCodeContainer registrationCodeContainer = RegistrationCodeContainer.getInstance();

	@GetMapping("/v1/users/{registrationCode}/organization-and-country")
	GetOrganizationAndCountryResponse handle(@PathVariable String registrationCode) {
		return registrationCodeContainer.getOrganizationAndCountry(registrationCode);
	}
}
