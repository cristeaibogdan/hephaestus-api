package org.personal.washingmachine.controller;

import lombok.RequiredArgsConstructor;
import org.personal.washingmachine.facade.UserFacade;
import org.personal.washingmachine.facade.dtos.OrganizationAndCountryDTO;
import org.personal.washingmachine.facade.dtos.UserCredentialsDTO;
import org.personal.washingmachine.facade.dtos.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
class UserController { //TODO: Replace with proper authentication
	private final UserFacade userFacade;

// ************************************************************
// *** REGISTRATION
// ************************************************************

	@GetMapping("/{registrationCode}")
	boolean isValidRegistrationCode(@PathVariable String registrationCode) {
		return userFacade.isValidRegistrationCode(registrationCode);
	}

	@GetMapping("/{registrationCode}/organization-and-country")
	OrganizationAndCountryDTO getOrganizationAndCountry(@PathVariable String registrationCode) {
		return userFacade.getOrganizationAndCountry(registrationCode);
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	void register(@RequestBody UserDTO userDTO) {
		userFacade.register(userDTO);
	}

// ************************************************************
// *** LOGIN
// ************************************************************

	@PostMapping("/login")
	UserDTO login(@RequestBody UserCredentialsDTO userCredentialsDTO) {
		return userFacade.login(userCredentialsDTO);
	}
}
