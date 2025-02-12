package org.personal.washingmachine.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.shared.exception.CustomException;
import org.personal.washingmachine.dto.GetOrganizationAndCountryResponse;
import org.personal.washingmachine.dto.UserMapper;
import org.personal.washingmachine.repository.UserRepository;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;

class GetOrganizationAndCountrySocialTest {
	UserRepository userRepositoryMock = mock(UserRepository.class);
	UserService userService = new UserService(userRepositoryMock);
	UserMapper userMapper = new UserMapper();
	UserApplicationService underTest = new UserApplicationService(userService, userMapper);

	static Stream<Arguments> getOrganizationAndCountryTestCases() {
		return Stream.of(
				arguments(new GetOrganizationAndCountryResponse("ZEOS", "SLOVENIA"), "RX1000"),
				arguments(new GetOrganizationAndCountryResponse("GORENJE", "SLOVENIA"), "RX2001"),
				arguments(new GetOrganizationAndCountryResponse("BOSCH", "GERMANY"), "RX3002"),
				arguments(new GetOrganizationAndCountryResponse("SMEG", "ITALY"), "RX4003"),
				arguments(new GetOrganizationAndCountryResponse("ORIGIN", "ROMANIA"), "RX5000")
		);
	}

	@ParameterizedTest(name = "Valid OrganizationAndCountryDTO for {1}")
	@MethodSource("getOrganizationAndCountryTestCases")
	void should_ReturnGetOrganizationAndCountryResponse_When_RegistrationCodeIsValid(
			GetOrganizationAndCountryResponse expected,
			String registrationCode) {
		// GIVEN

		// WHEN
		GetOrganizationAndCountryResponse actual = underTest.getOrganizationAndCountry(registrationCode);

		// THEN
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
	}
}
