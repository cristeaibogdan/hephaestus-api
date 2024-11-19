package org.personal.washingmachine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.personal.shared.clients.ProductClient;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WashingMachineApplicationService.class)
class GetManyExceptionMvcTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@MockBean WashingMachineService service;
	@MockBean WashingMachineRepository repository;
	@MockBean WashingMachineDamageCalculator damageCalculator;
	@MockBean WashingMachineReportGenerator reportGenerator;
	@MockBean ProductClient productClient; //TODO: To be deleted

	@Test
	void should_ThrowException_When_ListIsEmpty() throws Exception {
		// GIVEN
		List<String> content = new ArrayList<>();

		// WHEN
		ResultActions resultActions = mockMvc.perform(
				post("/api/v1/washing-machines/many")
						.content(jackson.writeValueAsString(content))
						.contentType(MediaType.APPLICATION_JSON));

		// THEN
		resultActions
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(containsString("Internal Translation Error"))));
	}

	@Test
	void should_ThrowException_When_NoSerialNumbersAreFoundInDB() throws Exception {
		// GIVEN
		List<String> content = new ArrayList<>(List.of(
				"I don't exist",
				"Something",
				"You won't find me"
		));

		// WHEN
		ResultActions resultActions = mockMvc.perform(
				post("/api/v1/washing-machines/many")
						.content(jackson.writeValueAsString(content))
						.contentType(MediaType.APPLICATION_JSON));

		// THEN
		resultActions
				.andExpect(status().isNotFound())
				.andExpect(content().string(containsString(content.get(0))))
				.andExpect(content().string(containsString(content.get(1))))
				.andExpect(content().string(containsString(content.get(2))));
	}
}
