package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.personal.washingmachine.BaseIntegrationTest;
import org.personal.washingmachine.TestData;
import org.personal.washingmachine.dto.LoginUserRequest;
import org.personal.washingmachine.dto.LoginUserResponse;
import org.personal.washingmachine.entity.User;
import org.personal.washingmachine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginIntegrationTest extends BaseIntegrationTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@Autowired UserApplicationService underTest;
	@Autowired UserRepository repository;

	@BeforeEach
	void checkNoDataInDB() {
		assertThat(repository.count()).isZero();
	}

	@Nested
	class IntegrationTest {

		@Test
		void should_ReturnDTO_With_AllPropertiesFromDB() {
			// GIVEN
			insertIntoDB(
					new User(
							"RX1001",
							"Bosch",
							"Poland",
							"unique@email.com",
							"unique_username",
							"somePassword"
					));

			LoginUserResponse expected = new LoginUserResponse(
					"RX1001",
					"Bosch",
					"Poland",
					"unique@email.com",
					"unique_username"
			);

			// WHEN
			LoginUserResponse actual = underTest.login(
					new LoginUserRequest("unique_username", "somePassword")
			);

			// THEN
			assertThat(actual).satisfies(act -> {
				assertThat(act.code()).isEqualTo(expected.code());
				assertThat(act.organization()).isEqualTo(expected.organization());
				assertThat(act.country()).isEqualTo(expected.country());
				assertThat(act.email()).isEqualTo(expected.email());
				assertThat(act.username()).isEqualTo(expected.username());
			});
		}
	}

	private void insertIntoDB(User... users) {
		repository.saveAll(List.of(users));
	}

	@Nested
	class MvcTest {

		@Test
		void should_ReturnStatusOk_When_UserFound() throws Exception {
			// GIVEN
			insertIntoDB(
					TestData.createUser().setUsername("foundUsername").setPassword("foundPass")
			);

			// WHEN
			ResultActions resultActions = performRequest(
					new LoginUserRequest("foundUsername", "foundPass")
			);

			// THEN
			resultActions
					.andExpect(status().isOk())
					.andExpect(content().string(not(emptyString())));
		}

		@Test
		void should_ThrowCustomException_When_CredentialsAreInvalid() throws Exception {
			// GIVEN
			insertIntoDB(
					TestData.createUser().setUsername("Johnny Cage").setPassword("qazwsx!")
			);

			// WHEN
			ResultActions resultActions = performRequest(
					new LoginUserRequest("Sasha", "12345")
			);

			// THEN
			resultActions
					.andExpect(status().isUnauthorized())
					.andExpect(content().string(not(containsString("Internal Translation Error"))));
		}

		private ResultActions performRequest(LoginUserRequest request) throws Exception {
			return mockMvc.perform(
					post("/api/v1/users/login")
							.content(jackson.writeValueAsString(request))
							.contentType(MediaType.APPLICATION_JSON));
		}
	}
}
