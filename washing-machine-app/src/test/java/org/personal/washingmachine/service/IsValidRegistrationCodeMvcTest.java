package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.personal.washingmachine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserApplicationService.class)
class IsValidRegistrationCodeMvcTest {
	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@MockBean UserService userService;

	@ParameterizedTest
	@ValueSource(strings = {"RX1001", "sdajosda", "RX2003", "   ", "  sa  "})
	void should_ReturnStatusOk_When_ProvidedAnyRegistrationCode(String registrationCode) throws Exception {
		// GIVEN
		// WHEN
		ResultActions resultActions = performRequest(registrationCode);

		// THEN
		resultActions
				.andExpect(status().isOk());
	}

	private ResultActions performRequest(String registrationCode) throws Exception {
		return mockMvc.perform(
				get("/api/v1/users/{registrationCode}", registrationCode)
		);
	}
}
