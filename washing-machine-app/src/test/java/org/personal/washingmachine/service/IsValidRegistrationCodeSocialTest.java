package org.personal.washingmachine.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.washingmachine.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class IsValidRegistrationCodeSocialTest {
	UserRepository userRepositoryMock = mock(UserRepository.class);
	UserService userService = new UserService(userRepositoryMock);
	UserApplicationService underTest = new UserApplicationService(userService);

	@ParameterizedTest(name = "Registration code {0} is valid")
	@ValueSource(strings = {"RX1000", "RX2001", "RX3002", "RX4003", "RX5000"})
	void should_ReturnTrue_When_RegistrationCodeValid(String registrationCode) {
		// GIVEN
		// WHEN
		boolean actual = underTest.isValidRegistrationCode(registrationCode);

		// THEN
		assertThat(actual).isTrue();
	}

	@ParameterizedTest(name = "Registration code {0} is invalid")
	@ValueSource(strings = {"2198asd", "   ", "21uh9us ", "", "%&^*!#@"})
	void should_ReturnFalse_When_RegistrationCodeInvalid(String registrationCode) {
		// GIVEN
		// WHEN
		boolean actual = underTest.isValidRegistrationCode(registrationCode);

		// THEN
		assertThat(actual).isFalse();
	}
}
