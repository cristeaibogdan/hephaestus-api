package org.personal.washingmachine.usecase.user.existsbyregistrationcode;

import org.personal.washingmachine.usecase.user.RegistrationCodeContainer;
import org.springframework.web.bind.annotation.*;

@RestController
class ExistsByRegistrationCode { // TODO: Replace with proper authentication
	private final RegistrationCodeContainer registrationCodeContainer = RegistrationCodeContainer.getInstance();

	@GetMapping("/v1/users/{registrationCode}/exists")
	boolean handle(@PathVariable String registrationCode) {
		return registrationCodeContainer.exists(registrationCode);
	}
}
