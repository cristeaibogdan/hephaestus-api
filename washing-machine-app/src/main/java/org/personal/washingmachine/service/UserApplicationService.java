package org.personal.washingmachine.service;

import lombok.RequiredArgsConstructor;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.entity.User;
import org.personal.washingmachine.dto.OrganizationAndCountryDTO;
import org.personal.washingmachine.dto.LoginUserRequest;
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
	private final Map<OrganizationAndCountryDTO, List<String>> registerCodes = initializeRegistrationCodes();

	private Map<OrganizationAndCountryDTO, List<String>> initializeRegistrationCodes() {
		Map<OrganizationAndCountryDTO, List<String>> registerCodes = new HashMap<>();
		registerCodes.put(new OrganizationAndCountryDTO("ZEOS", "SLOVENIA"), List.of("RX1000", "RX1001", "RX1002", "RX1003"));
		registerCodes.put(new OrganizationAndCountryDTO("GORENJE", "SLOVENIA"), List.of("RX2000", "RX2001", "RX2002", "RX2003"));
		registerCodes.put(new OrganizationAndCountryDTO("BOSCH", "GERMANY"), List.of("RX3000", "RX3001", "RX3002", "RX3003"));
		registerCodes.put(new OrganizationAndCountryDTO("SMEG", "ITALY"), List.of("RX4000", "RX4001", "RX4002", "RX4003"));
		registerCodes.put(new OrganizationAndCountryDTO("ORIGIN", "ROMANIA"), List.of("RX5000", "RX5001", "RX5002", "RX5003"));

		return registerCodes;
	}

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
	public UserDTO login(LoginUserRequest loginUserRequest) {
		User user = service.login(loginUserRequest.username(), loginUserRequest.password());
		return UserMapper.toDTO(user);
	}
}
