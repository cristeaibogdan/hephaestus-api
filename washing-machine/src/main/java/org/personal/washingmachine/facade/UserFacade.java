package org.personal.washingmachine.facade;

import lombok.RequiredArgsConstructor;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.entity.User;
import org.personal.washingmachine.dto.Mapper;
import org.personal.washingmachine.dto.OrganizationAndCountryDTO;
import org.personal.washingmachine.dto.UserCredentialsDTO;
import org.personal.washingmachine.dto.UserDTO;
import org.personal.washingmachine.service.UserService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserFacade {
	private final UserService userService;

	private Map<OrganizationAndCountryDTO, List<String>> initializeRegistrationCodes() {
		Map<OrganizationAndCountryDTO, List<String>> registerCodes = new HashMap<>();
		registerCodes.put(new OrganizationAndCountryDTO("ZEOS", "SLOVENIA"), List.of("RX1000", "RX1001", "RX1002", "RX1003"));
		registerCodes.put(new OrganizationAndCountryDTO("GORENJE", "SLOVENIA"), List.of("RX2000", "RX2001", "RX2002", "RX2003"));
		registerCodes.put(new OrganizationAndCountryDTO("BOSCH", "GERMANY"), List.of("RX3000", "RX3001", "RX3002", "RX3003"));
		registerCodes.put(new OrganizationAndCountryDTO("SMEG", "ITALY"), List.of("RX4000", "RX4001", "RX4002", "RX4003"));
		registerCodes.put(new OrganizationAndCountryDTO("ORIGIN", "ROMANIA"), List.of("RX5000", "RX5001", "RX5002", "RX5003"));

		return registerCodes;
	}

	public boolean isValidRegistrationCode(String registrationCode) {
		// TODO: learn about getOrDefault()
		Optional<OrganizationAndCountryDTO> response = initializeRegistrationCodes().entrySet().stream()
				.filter(entry -> entry.getValue().contains(registrationCode))
				.map(entry -> entry.getKey())
				.findFirst();

		return response.isPresent();
	}

	public OrganizationAndCountryDTO getOrganizationAndCountry(String registrationCode) {
		return initializeRegistrationCodes().entrySet()
				.stream()
				.filter(entry -> entry.getValue().contains(registrationCode))
				.map(entry -> entry.getKey())
				.findFirst()
				.orElseThrow(() -> new CustomException(ErrorCode.INVALID_REGISTRATION_CODE));
	}

	public void register(UserDTO userDTO) {
		//TODO: 1. Should I check if the user exists in HERE? or in the service?
		User user = Mapper.UserMapper.toEntity(userDTO);
		userService.register(user);
	}

	public UserDTO login(UserCredentialsDTO userCredentialsDTO) {
		User user = userService.login(userCredentialsDTO);
		return Mapper.UserMapper.toDTO(user);
	}
}
