package org.personal.washingmachine.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.personal.shared.exception.CustomException;
import org.personal.washingmachine.entity.User;
import org.personal.washingmachine.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@InjectMocks
	private UserService underTest;

	@Mock
	private UserRepository userRepositoryMock;

	private final User user = new User(
			"RX1001",
			"ZEOS",
			"ROMANIA",
			"some@email.com",
			"testUsername",
			"testPassword"
	);

	@Nested
	class testRegister {

		@Test
		void should_ThrowCustomException_When_EmailIsAlreadyTaken() {
			// GIVEN
			user.setEmail("taken@email.com");

			given(userRepositoryMock.existsByEmail(user.getEmail()))
					.willReturn(true);

			// WHEN & THEN
			assertThatThrownBy(() -> underTest.register(user))
					.isInstanceOf(CustomException.class);
		}

		@Test
		void should_ThrowCustomException_When_UsernameIsAlreadyTaken() {
			// GIVEN
			user.setUsername("takenUsername");

			given(userRepositoryMock.existsByUsername(user.getUsername()))
					.willReturn(true);

			// WHEN & THEN
			assertThatThrownBy(() -> underTest.register(user))
					.isInstanceOf(CustomException.class);
		}

		@Test
		void should_SaveUser_When_ValidUserProvided() {
			// GIVEN

			// WHEN
			underTest.register(user);

			// THEN
			then(userRepositoryMock)
					.should(times(1))
					.save(user);
		}
	}

	@Nested
	class TestLogin {

		@Test
		void should_ThrowCustomException_When_BadCredentials() {
			// GIVEN
			String username = "badUsername";
			String password = "badPassword";

			// WHEN & THEN
			Assertions.assertThatThrownBy(() -> underTest.login(username, password))
					.isInstanceOf(CustomException.class);
		}

		@Test
		void should_ReturnUser_When_GoodCredentials() {
			// GIVEN
			String username = "goodUsername";
			String password = "goodPassword";

			User expected = new User(
					null,
					null,
					null,
					null,
					username,
					password
			);

			given(userRepositoryMock.findByUsernameAndPassword(username, password))
					.willReturn(Optional.of(new User(null, null, null, null, username, password)));

			// WHEN
			User actual = underTest.login(username, password);

			// THEN
			assertThat(actual)
					.usingRecursiveComparison()
					.isEqualTo(expected);
		}
	}

}