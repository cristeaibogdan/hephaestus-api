package org.personal.washingmachine.usecase.user.getorganizationandcountry;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GetOrganizationAndCountry.class)
class GetOrganizationAndCountryMvcTest {
	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@Test
	void should_ReturnStatusOk_When_ValidCode() throws Exception {
		// GIVEN
		// WHEN
		ResultActions resultActions = performRequest("RX1001");

		// THEN
		resultActions
				.andExpect(status().isOk())
				.andExpect(content().string(not(emptyString())));
	}

	@Test
	void should_ThrowCustomException_When_InvalidCode() throws Exception {
		// GIVEN
		// WHEN
		ResultActions resultActions = performRequest("saijdoa7821z");

		// THEN
		resultActions
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(containsString("Internal Translation Error"))));
	}

	private ResultActions performRequest(String registrationCode) throws Exception {
		return mockMvc.perform(
				get("/v1/users/{registrationCode}/organization-and-country", registrationCode));
	}
}
