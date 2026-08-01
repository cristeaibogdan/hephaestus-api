package org.personal.washingmachine.service;

import lombok.RequiredArgsConstructor;
import org.personal.washingmachine.dto.CreateUserRequest;
import org.personal.washingmachine.dto.GetOrganizationAndCountryResponse;
import org.personal.washingmachine.dto.LoginUserRequest;
import org.personal.washingmachine.dto.LoginUserResponse;
import org.personal.washingmachine.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.personal.washingmachine.mapper.UserMapper;

@RestController
@RequiredArgsConstructor
class UserApplicationService { // TODO: Replace with proper authentication
	private final UserService service;
	private final UserMapper userMapper;
	private final RegistrationCodeContainer registrationCodeContainer = RegistrationCodeContainer.getInstance();

	@GetMapping("/api/v1/users/{registrationCode}")
	public boolean isValidRegistrationCode(@PathVariable String registrationCode) {
		return registrationCodeContainer.exists(registrationCode);
	}

	@GetMapping("/api/v1/users/{registrationCode}/organization-and-country")
	public GetOrganizationAndCountryResponse getOrganizationAndCountry(@PathVariable String registrationCode) {
		return registrationCodeContainer.getOrganizationAndCountry(registrationCode);
	}

	@PostMapping("/api/v1/users/register")
	@ResponseStatus(HttpStatus.CREATED)
	public void register(CreateUserRequest createUserRequest) {
		User user = userMapper.toEntity(createUserRequest);
		service.register(user);
	}

	@PostMapping("/api/v1/users/login")
	public LoginUserResponse login(LoginUserRequest loginUserRequest) {
		User user = service.login(loginUserRequest.username(), loginUserRequest.password());
		return userMapper.toLoginUserResponse(user);
	}
}
