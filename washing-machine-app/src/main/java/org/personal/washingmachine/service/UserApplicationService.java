package org.personal.washingmachine.service;

import lombok.RequiredArgsConstructor;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.entity.User;
import org.personal.washingmachine.dto.OrganizationAndCountryDTO;
import org.personal.washingmachine.dto.UserCredentialsDTO;
import org.personal.washingmachine.dto.UserDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.personal.washingmachine.dto.Mapper.*;

@Service
@RestController
@RequiredArgsConstructor
public class UserApplicationService implements IUserApplicationService { // TODO: Replace with proper authentication
	private final UserService service;
	private final Map<OrganizationAndCountryDTO, List<String>> registerCodes;

	@Override
	public boolean isValidRegistrationCode(String registrationCode) {
		Optional<OrganizationAndCountryDTO> response = registerCodes.entrySet().stream()
				.filter(entry -> entry.getValue().contains(registrationCode))
				.map(entry -> entry.getKey())
				.findFirst();

		return response.isPresent();
	}

	@Override
	public OrganizationAndCountryDTO getOrganizationAndCountry(String registrationCode) {
		return registerCodes.entrySet()
				.stream()
				.filter(entry -> entry.getValue().contains(registrationCode))
				.map(entry -> entry.getKey())
				.findFirst()
				.orElseThrow(() -> new CustomException(ErrorCode.INVALID_REGISTRATION_CODE));
	}

	@Override
	public void register(UserDTO userDTO) {
		User user = UserMapper.toEntity(userDTO);
		service.register(user);
	}

	@Override
	public UserDTO login(UserCredentialsDTO userCredentialsDTO) {
		User user = service.login(userCredentialsDTO.username(), userCredentialsDTO.password());
		return UserMapper.toDTO(user);
	}
}
