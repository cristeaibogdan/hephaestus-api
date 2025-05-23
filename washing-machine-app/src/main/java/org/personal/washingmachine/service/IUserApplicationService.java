package org.personal.washingmachine.service;

import org.personal.washingmachine.dto.CreateUserRequest;
import org.personal.washingmachine.dto.GetOrganizationAndCountryResponse;
import org.personal.washingmachine.dto.LoginUserRequest;
import org.personal.washingmachine.dto.LoginUserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/users")
public interface IUserApplicationService {
	@GetMapping("/{registrationCode}")
	boolean isValidRegistrationCode(@PathVariable String registrationCode);

	@GetMapping("/{registrationCode}/organization-and-country")
	GetOrganizationAndCountryResponse getOrganizationAndCountry(@PathVariable String registrationCode);

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	void register(@RequestBody CreateUserRequest createUserRequest);

	@PostMapping("/login")
	LoginUserResponse login(@RequestBody LoginUserRequest loginUserRequest);
}
