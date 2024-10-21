package org.personal.washingmachine.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.shared.exception.CustomException;
import org.personal.washingmachine.dto.OrganizationAndCountryDTO;
import org.personal.washingmachine.dto.LoginUserRequest;
import org.personal.washingmachine.dto.UserDTO;
import org.personal.washingmachine.repository.UserRepository;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.personal.washingmachine.dto.Mapper.UserMapper;

class UserApplicationServiceTest {

	UserRepository userRepositoryMock = mock(UserRepository.class);
	UserService userService = new UserService(userRepositoryMock);

	UserApplicationService underTest = new UserApplicationService(userService);

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
			UserDTO dto = new UserDTO(
					"code",
					"ORG",
					"ROMANIA",
					"email@yahoo.com",
					"user123456",
					"pass123456");

			// WHEN
			underTest.register(dto);

			// THEN
//			then(userRepositoryMock)
//					.should(times(1))
//					.save(UserMapper.toEntity(dto));
		}

		@Test
		void should_ThrowCustomException_When_EmailIsAlreadyTaken() {
			// GIVEN
			UserDTO dto = new UserDTO(
					"code",
					"ORG",
					"ROMANIA",
					"takenEmail@yahoo.com",
					"user123456",
					"pass123456");

			given(userRepositoryMock.existsByEmail(dto.email()))
					.willReturn(true);

			// When & THEN
			assertThatThrownBy(() -> underTest.register(dto))
					.isInstanceOf(CustomException.class);
		}

		@Test
		void should_ThrowCustomException_When_UsernamesAlreadyTaken() {
			// GIVEN
			UserDTO dto = new UserDTO(
					"code",
					"ORG",
					"ROMANIA",
					"email@yahoo.com",
					"takenUser",
					"pass123456");

			given(userRepositoryMock.existsByUsername(dto.username()))
					.willReturn(true);

			// When & THEN
			assertThatThrownBy(() -> underTest.register(dto))
					.isInstanceOf(CustomException.class);
		}
	}

	@Nested
	class TestLogin {

		@Test
		void should_ReturnUserDTO_When_GoodCredentials() {
			// GIVEN
			LoginUserRequest credentials = new LoginUserRequest("User", "Pass");

			UserDTO expected = new UserDTO(
					"RX1001",
					"Origin",
					"Romania",
					"someEmail@yahoo.com",
					credentials.username(),
					null
			);

			given(userRepositoryMock.findByUsernameAndPassword(credentials.username(), credentials.password()))
					.willReturn(Optional.of(UserMapper.toEntity(expected)));

			// WHEN
			UserDTO actual = underTest.login(credentials);

			// THEN
			assertThat(actual)
					.usingRecursiveComparison()
					.isEqualTo(expected);
		}

		@Test
		void should_ThrowException_When_BadCredentials() {
			// GIVEN
			LoginUserRequest credentials = new LoginUserRequest("UserBad", "PassBad");

			given(userRepositoryMock.findByUsernameAndPassword(credentials.username(), credentials.password()))
					.willThrow(CustomException.class);

			// WHEN & THEN
			assertThatThrownBy(() -> underTest.login(credentials))
					.isInstanceOf(CustomException.class);
		}
	}
}
