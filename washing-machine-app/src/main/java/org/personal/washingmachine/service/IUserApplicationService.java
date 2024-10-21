package org.personal.washingmachine.service;

import org.personal.washingmachine.dto.GetOrganizationAndCountryResponse;
import org.personal.washingmachine.dto.LoginUserRequest;
import org.personal.washingmachine.dto.UserDTO;
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
	void register(@RequestBody UserDTO userDTO);

	@PostMapping("/login")
	UserDTO login(@RequestBody LoginUserRequest loginUserRequest);
}
