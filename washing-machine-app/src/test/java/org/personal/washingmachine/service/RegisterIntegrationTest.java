package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.personal.washingmachine.BaseIntegrationTest;
import org.personal.washingmachine.TestData;
import org.personal.washingmachine.dto.CreateUserRequest;
import org.personal.washingmachine.dto.GetWashingMachineReportResponse;
import org.personal.washingmachine.entity.User;
import org.personal.washingmachine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.atomicReferenceArray;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional // Ensure Hibernate sessions are properly managed in your testing environment. Avoids "could not initialize proxy" Exception. Specific to tests only.
class RegisterIntegrationTest extends BaseIntegrationTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@Autowired UserApplicationService underTest;
	@Autowired UserRepository repository;

	@BeforeEach
	void checkNoDataInDB() {
		assertThat(repository.count()).isZero();
	}

	@Test
	void should_saveRequest_With_AllPropertiesInDB() {
		// GIVEN
		CreateUserRequest request = new CreateUserRequest(
				"RX1001",
				"Bosch",
				"Poland",
				"unique@email.com",
				"unique_username",
				"somePassword"
		);

		// WHEN
		underTest.register(request);

		// THEN
		User actual = repository.findByUsernameAndPassword("unique_username", "somePassword")
				.orElseThrow();

		assertThat(actual).satisfies(act -> {
			assertThat(act.getCode()).isEqualTo(request.code());
			assertThat(act.getOrganization()).isEqualTo(request.organization());
			assertThat(act.getCountry()).isEqualTo(request.country());
			assertThat(act.getEmail()).isEqualTo(request.email());
			assertThat(act.getUsername()).isEqualTo(request.username());
			assertThat(act.getPassword()).isEqualTo(request.password());
			assertThat(act.getCreatedAt().toLocalDate()).isEqualTo(LocalDate.now());
		});
	}

	private void insertIntoDB(User... users) {
		repository.saveAll(List.of(users));
	}

	@Nested
	class MvcTest {

		@Test
		void should_ReturnStatusOk_When_DTOSaved() throws Exception {
			// GIVEN
			// WHEN
			ResultActions resultActions = performRequest(
					TestData.createUserRequest()
			);

			// THEN
			resultActions
					.andExpect(status().isCreated())
					.andExpect(content().string(emptyString()));
		}

		@Test
		void should_ThrowCustomException_When_EmailAlreadyTaken() throws Exception {
			// GIVEN
			insertIntoDB(
					TestData.createUser().setEmail("takenEmail@yahoo.com")
			);

			// WHEN
			ResultActions resultActions = performRequest(TestData.createUserRequest().toBuilder()
					.email("takenEmail@yahoo.com")
					.build());

			// THEN
			resultActions
					.andExpect(status().isConflict())
					.andExpect(content().string(not(containsString("Internal Translation Error"))));
		}

		@Test
		void should_ThrowCustomException_When_UsernameAlreadyTaken() throws Exception {
			// GIVEN
			insertIntoDB(
					TestData.createUser().setEmail("someEmail@something.com").setUsername("Taken")
			);

			// WHEN
			ResultActions resultActions = performRequest(TestData.createUserRequest().toBuilder()
					.username("Taken")
					.build());

			// THEN
			resultActions
					.andExpect(status().isConflict())
					.andExpect(content().string(not(containsString("Internal Translation Error"))));
		}

		private ResultActions performRequest(CreateUserRequest request) throws Exception {
			return mockMvc.perform(
					post("/api/v1/users/register")
							.content(jackson.writeValueAsString(request))
							.contentType(MediaType.APPLICATION_JSON));
		}
	}
}
