package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.personal.shared.clients.ProductClient;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WashingMachineApplicationService.class)
class GetRecommendationMvcTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@MockBean WashingMachineService service;
	@MockBean WashingMachineRepository repository;
	@MockBean WashingMachineDamageCalculator damageCalculator;
	@MockBean WashingMachineReportGenerator reportGenerator;
	@MockBean ProductClient productClient; //TODO: To be deleted

	@Test
	void should_ReturnStatusOk() throws Exception {
		// GIVEN
		String serialNumber = "ABC-987";
		Recommendation expected = Recommendation.RESALE;

		given(repository.getRecommendation(serialNumber))
				.willReturn(Optional.of(expected));

		// WHEN
		ResultActions resultActions = performRequest(serialNumber);

		// THEN
		resultActions
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(String.valueOf(expected))));
	}

	@Test
	void should_ThrowCustomException_When_SerialNumberNotFound() throws Exception {
		// GIVEN
		String serialNumber = "I don't exist";

		// WHEN
		ResultActions resultActions = performRequest(serialNumber);

		// THEN
		resultActions
				.andExpect(status().isNotFound())
				.andExpect(content().string(not(containsString("Internal Translation Error"))));
	}

	private ResultActions performRequest(String serialNumber) throws Exception {
		return mockMvc.perform(
				get("/api/v1/washing-machines/{serialNumber}/recommendation", serialNumber)
		);
	}
}
