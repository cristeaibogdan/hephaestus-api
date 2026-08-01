package org.personal.washingmachine.usecase.user.checkregistrationcodeexists;

import lombok.RequiredArgsConstructor;
import org.personal.washingmachine.usecase.user.RegistrationCodeContainer;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
class CheckRegistrationCodeExists { // TODO: Replace with proper authentication
	private final RegistrationCodeContainer registrationCodeContainer = RegistrationCodeContainer.getInstance();

	@GetMapping("/api/v1/users/{registrationCode}")
	boolean handle(@PathVariable String registrationCode) {
		return registrationCodeContainer.exists(registrationCode);
	}
}
