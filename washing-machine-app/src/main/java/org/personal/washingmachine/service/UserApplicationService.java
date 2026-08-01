package org.personal.washingmachine.service;

import lombok.RequiredArgsConstructor;
import org.personal.washingmachine.dto.GetOrganizationAndCountryResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
class UserApplicationService { // TODO: Replace with proper authentication
	private final RegistrationCodeContainer registrationCodeContainer = RegistrationCodeContainer.getInstance();

	@GetMapping("/api/v1/users/{registrationCode}")
	public boolean isValidRegistrationCode(@PathVariable String registrationCode) {
		return registrationCodeContainer.exists(registrationCode);
	}

	@GetMapping("/api/v1/users/{registrationCode}/organization-and-country")
	public GetOrganizationAndCountryResponse getOrganizationAndCountry(@PathVariable String registrationCode) {
		return registrationCodeContainer.getOrganizationAndCountry(registrationCode);
	}
}
