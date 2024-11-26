package org.personal.washingmachine.service;

import lombok.RequiredArgsConstructor;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.dto.CreateUserRequest;
import org.personal.washingmachine.entity.User;
import org.personal.washingmachine.dto.GetOrganizationAndCountryResponse;
import org.personal.washingmachine.dto.LoginUserRequest;
import org.personal.washingmachine.dto.LoginUserResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.personal.washingmachine.dto.Mapper.*;

@Service
@RestController
@RequiredArgsConstructor
public class UserApplicationService implements IUserApplicationService { // TODO: Replace with proper authentication
	private final UserService service;
	private final Map<GetOrganizationAndCountryResponse, List<String>> registerCodes = RegistrationCodes.getInstance().getCodes();

	@Override
	public boolean isValidRegistrationCode(String registrationCode) {
		Optional<GetOrganizationAndCountryResponse> response = registerCodes.entrySet().stream()
				.filter(entry -> entry.getValue().contains(registrationCode))
				.map(entry -> entry.getKey())
				.findFirst();

		return response.isPresent();
	}

	@Override
	public GetOrganizationAndCountryResponse getOrganizationAndCountry(String registrationCode) {
		return registerCodes.entrySet()
				.stream()
				.filter(entry -> entry.getValue().contains(registrationCode))
				.map(entry -> entry.getKey())
				.findFirst()
				.orElseThrow(() -> new CustomException(ErrorCode.INVALID_REGISTRATION_CODE));
	}

	@Override
	public void register(CreateUserRequest createUserRequest) {
		User user = UserMapper.toEntity(createUserRequest);
		service.register(user);
	}

	@Override
	public LoginUserResponse login(LoginUserRequest loginUserRequest) {
		User user = service.login(loginUserRequest.username(), loginUserRequest.password());
		return UserMapper.toLoginUserResponse(user);
	}
}
