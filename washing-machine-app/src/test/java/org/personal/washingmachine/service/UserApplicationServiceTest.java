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
