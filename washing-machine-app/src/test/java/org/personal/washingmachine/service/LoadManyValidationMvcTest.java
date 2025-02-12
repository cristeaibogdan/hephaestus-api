package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.personal.shared.clients.ProductClient;
import org.personal.washingmachine.mapper.WashingMachineImageMapper;
import org.personal.washingmachine.mapper.WashingMachineMapper;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WashingMachineApplicationService.class)
class LoadManyValidationMvcTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@MockBean WashingMachineService service;
	@MockBean WashingMachineRepository repository;
	@MockBean WashingMachineDamageCalculator damageCalculator;
	@MockBean WashingMachineReportGenerator reportGenerator;
	@MockBean WashingMachineImageMapper washingMachineImageMapper;
	@MockBean WashingMachineMapper washingMachineMapper;
	@MockBean ProductClient productClient; //TODO: To be deleted

	@Test
	void should_ThrowValidationException_When_SerialNumbersIsEmpty() throws Exception {
		// GIVEN
		// WHEN
		ResultActions resultActions = performRequest(Set.of());

		// THEN
		resultActions
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(containsString("{"))));
	}

	@Test
	void should_ThrowValidationException_When_SerialNumbersContainNull() throws Exception {
		// GIVEN
		Set<String> request = new HashSet<>();
		request.add(null);
		request.add("serial1");
		request.add("serial2");

		// WHEN
		ResultActions resultActions = performRequest(request);

		// THEN
		resultActions
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(containsString("{"))));
	}

	@Test
	void should_ThrowValidationException_When_SerialNumbersContainEmptyString() throws Exception {
		// GIVEN
		// WHEN
		ResultActions resultActions = performRequest(Set.of("   ", ""));

		// THEN
		resultActions
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(containsString("{"))));
	}

	private ResultActions performRequest(Set<String> request) throws Exception {
		return mockMvc.perform(
				post("/api/v1/washing-machines/many")
						.content(jackson.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON));
	}
}
