package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.personal.washingmachine.BaseIntegrationTest;
import org.personal.washingmachine.TestData;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class IsSerialNumberInUseIntegrationTest extends BaseIntegrationTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@Autowired WashingMachineApplicationService underTest;
	@Autowired WashingMachineRepository repository;

	@BeforeEach
	void checkNoDataInDB() {
		assertThat(repository.count()).isZero();
	}

	@Test
	void should_ReturnStatusOk_WhenSerialNumberFound() throws Exception {
		// GIVEN
		saveToDB(TestData.createValidWashingMachine("ABC-987"));

		// WHEN
		ResultActions resultActions = performRequest("ABC-987");

		// THEN
		resultActions
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("true")));
	}

	@Test
	void should_ReturnStatusOk_WhenSerialNumberNotFound() throws Exception {
		// GIVEN
		// WHEN
		ResultActions resultActions = performRequest("not found");

		// THEN
		resultActions
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("false")));
	}


	private void saveToDB(WashingMachine... washingMachines) {
		repository.saveAll(List.of(washingMachines));
	}

	private ResultActions performRequest(String serialNumber) throws Exception {
		return mockMvc.perform(
				get("/api/v1/washing-machines/{serialNumber}/validate", serialNumber)
		);
	}
}
