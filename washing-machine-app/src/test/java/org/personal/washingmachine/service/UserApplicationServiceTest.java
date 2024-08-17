package org.personal.washingmachine.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.personal.shared.exception.CustomException;
import org.personal.washingmachine.dto.OrganizationAndCountryDTO;
import org.personal.washingmachine.dto.UserCredentialsDTO;
import org.personal.washingmachine.dto.UserDTO;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.personal.washingmachine.dto.Mapper.UserMapper;

@ExtendWith(MockitoExtension.class)
class UserApplicationServiceTest {

	@InjectMocks
	private UserApplicationService underTest;

	@Mock
	private UserService userServiceMock;

	@Nested
	class TestIsValidRegistrationCode {

		@ParameterizedTest(name = "{0} is a valid registration code")
		@ValueSource(strings = {
				"RX1000", "RX1001", "RX1002", "RX1003",
				"RX2000", "RX2001", "RX2002", "RX2003",
				"RX3000", "RX3001", "RX3002", "RX3003",
				"RX4000", "RX4001", "RX4002", "RX4003",
		})
		void should_ReturnTrue_When_RegistrationCodeIsValid(String registrationCode) {
			// GIVEN

			// WHEN
			Boolean actual = underTest.isValidRegistrationCode(registrationCode);

			// THEN
			assertThat(actual).isTrue();
		}

		@ParameterizedTest(name = "{0} is not a valid registration code")
		@ValueSource(strings = {"RX1234", "   ", "test", "something"})
		void should_ReturnFalse_When_RegistrationCodeIsInvalid(String registrationCode) {
			// GIVEN

			// WHEN
			Boolean actual = underTest.isValidRegistrationCode(registrationCode);

			// THEN
			assertThat(actual).isFalse();
		}
	}

	@Nested
	class TestGetOrganizationAndCountry {

		static Stream<Arguments> getOrganizationAndCountryTestCases() {
			return Stream.of(
					arguments(new OrganizationAndCountryDTO("ZEOS", "SLOVENIA"), "RX1000"),
					arguments(new OrganizationAndCountryDTO("GORENJE", "SLOVENIA"), "RX2001"),
					arguments(new OrganizationAndCountryDTO("BOSCH", "GERMANY"), "RX3002"),
					arguments(new OrganizationAndCountryDTO("SMEG", "ITALY"), "RX4003"),
					arguments(new OrganizationAndCountryDTO("ORIGIN", "ROMANIA"), "RX5000")
			);
		}

		@ParameterizedTest(name = "Valid OrganizationAndCountryDTO for {1}")
		@MethodSource("getOrganizationAndCountryTestCases")
		void should_ReturnOrganizationAndCountryDTO_When_RegistrationCodeIsValid(
				OrganizationAndCountryDTO expected,
				String registrationCode) {
			// GIVEN

			// WHEN
			OrganizationAndCountryDTO actual = underTest.getOrganizationAndCountry(registrationCode);

			// THEN
			assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
		}

		@ParameterizedTest(name = "{0} is not a valid registration code")
		@ValueSource(strings = {"RX1234", "   ", "test", "something"})
		void should_ThrowCustomException_When_RegistrationCodeIsInvalid(String registrationCode) {
			// GIVEN

			// WHEN & THEN
			assertThatThrownBy(() -> underTest.getOrganizationAndCountry(registrationCode))
					.isInstanceOf(CustomException.class);
		}
	}

	@Nested
	class TestRegister {

		@Test
		void should_SaveUser_When_ValidUserProvided() {
			// GIVEN
			UserDTO userDTO = new UserDTO(
					"code",
					"ORG",
					"ROMANIA",
					"email@yahoo.com",
					"user123456",
					"pass123456");

			// WHEN
			underTest.register(userDTO);

			// THEN
			then(userServiceMock)
					.should(times(1))
					.register(any());
		}
	}

	@Nested
	class TestLogin {

		@Test
		void should_ReturnUserDTO_When_GoodCredentials() {
			// GIVEN
			UserCredentialsDTO userCredentialsDTO = new UserCredentialsDTO(
					"User",
					"Pass");

			UserDTO expected = UserDTO.builder()
					.username("usernameTest")
					.password(null)
					.build();

			given(userServiceMock.login(userCredentialsDTO.username(), userCredentialsDTO.password()))
					.willReturn(UserMapper.toEntity(expected));

			// WHEN
			UserDTO actual = underTest.login(userCredentialsDTO);

			// THEN
			assertThat(actual)
					.usingRecursiveComparison()
					.isEqualTo(expected);
		}
	}
}
