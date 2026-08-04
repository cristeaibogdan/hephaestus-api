package org.personal.washingmachine.usecase.user.existsbyregistrationcode;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExistsByRegistrationCode.class)
class ExistsByRegistrationCodeMvcTest {
	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

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
				get("/v1/users/{registrationCode}/exists", registrationCode)
		);
	}
}
