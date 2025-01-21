package org.personal.washingmachine.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.shared.exception.CustomException;
import org.personal.washingmachine.dto.CreateUserRequest;
import org.personal.washingmachine.dto.GetOrganizationAndCountryResponse;
import org.personal.washingmachine.dto.LoginUserRequest;
import org.personal.washingmachine.dto.LoginUserResponse;
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
	class TestRegister {

		@Test
		void should_SaveUser_When_ValidUserProvided() {
			// GIVEN
			CreateUserRequest dto = new CreateUserRequest(
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
			CreateUserRequest dto = new CreateUserRequest(
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
			CreateUserRequest dto = new CreateUserRequest(
					"code",
					"ORG",
					"ROMANIA",
					"email@yahoo.com",
					"takenUser",
					"pass123456");

			given(userRepositoryMock.existsByUsername(dto.username()))
					.willReturn(true);

			// WHEN & THEN
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

			CreateUserRequest expected = new CreateUserRequest(
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
			LoginUserResponse actual = underTest.login(credentials);

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
