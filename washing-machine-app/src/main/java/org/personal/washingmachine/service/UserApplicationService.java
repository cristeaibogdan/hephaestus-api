package org.personal.washingmachine.service;

import lombok.RequiredArgsConstructor;
import org.personal.washingmachine.dto.CreateUserRequest;
import org.personal.washingmachine.dto.GetOrganizationAndCountryResponse;
import org.personal.washingmachine.dto.LoginUserRequest;
import org.personal.washingmachine.dto.LoginUserResponse;
import org.personal.washingmachine.entity.User;
import org.springframework.web.bind.annotation.RestController;

import org.personal.washingmachine.mapper.UserMapper;

@RestController
@RequiredArgsConstructor
class UserApplicationService implements IUserApplicationService { // TODO: Replace with proper authentication
	private final UserService service;
	private final UserMapper userMapper;
	private final RegistrationCodeContainer registrationCodeContainer = RegistrationCodeContainer.getInstance();

	@Override
	public boolean isValidRegistrationCode(String registrationCode) {
		return registrationCodeContainer.exists(registrationCode);
	}

	@Override
	public GetOrganizationAndCountryResponse getOrganizationAndCountry(String registrationCode) {
		return registrationCodeContainer.getOrganizationAndCountry(registrationCode);
	}

	@Override
	public void register(CreateUserRequest createUserRequest) {
		User user = userMapper.toEntity(createUserRequest);
		service.register(user);
	}

	@Override
	public LoginUserResponse login(LoginUserRequest loginUserRequest) {
		User user = service.login(loginUserRequest.username(), loginUserRequest.password());
		return userMapper.toLoginUserResponse(user);
	}
}
