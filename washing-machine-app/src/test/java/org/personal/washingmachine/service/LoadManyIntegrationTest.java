package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.personal.washingmachine.BaseIntegrationTest;
import org.personal.washingmachine.TestData;
import org.personal.washingmachine.dto.GetWashingMachineFullResponse;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoadManyIntegrationTest extends BaseIntegrationTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@Autowired WashingMachineApplicationService underTest;
	@Autowired WashingMachineRepository repository;

	@BeforeEach
	void checkInitialDataInDB() {
		assertThat(repository.count()).isZero();
	}

	@Nested
	class IntegrationTest {

		@Test
		void should_ReturnDTOs_When_SerialNumbersFound() {
			// GIVEN
			saveToDB(
					TestData.createValidWashingMachine("serial1"),
					TestData.createValidWashingMachine("serial2"),
					TestData.createValidWashingMachine("serial3")
			);

			// WHEN
			Map<String, GetWashingMachineFullResponse> actual = underTest.loadMany(Set.of("serial1", "serial2"));

			// THEN
			assertThat(actual)
					.containsOnlyKeys("serial1", "serial2");

			assertThat(actual.values())
					.extracting(wm -> wm.serialNumber())
					.containsOnly("serial1", "serial2");
		}

		@Test
		void should_ReturnNullDTOs_When_SerialNumbersNotFound() {
			// GIVEN
			saveToDB(
					TestData.createValidWashingMachine("serial1"),
					TestData.createValidWashingMachine("serial2")
			);

			Map<String, GetWashingMachineFullResponse> notFoundMap = new HashMap<>();
			notFoundMap.put("I don't exist", null);
			notFoundMap.put("Nothing", null);

			// WHEN
			Map<String, GetWashingMachineFullResponse> actual = underTest.loadMany(Set.of(
					"serial1",
					"serial2",
					"I don't exist",
					"Nothing"
			));

			// THEN
			assertThat(actual)
					.containsOnlyKeys("I don't exist", "serial1", "serial2", "Nothing")
					.containsAllEntriesOf(notFoundMap);
		}
	}

	private void saveToDB(WashingMachine... washingMachines) {
		repository.saveAll(List.of(washingMachines));
	}

	@Nested
	class MvcTest {

		@Test
		void should_ThrowCustomException_When_SerialNumbersNotFound() throws Exception {
			// GIVEN
			// WHEN
			ResultActions resultActions = performRequest(
					Set.of("I don't exist", "You won't find me")
			);

			// THEN
			resultActions
					.andExpect(status().isNotFound())
					.andExpect(content().string(not(containsString("Internal Translation Error"))))
					.andExpect(content().string(containsString("I don't exist")))
					.andExpect(content().string(containsString("You won't find me")));
		}

		@Test
		void should_ReturnStatusOk_When_SerialNumbersFound() throws Exception {
			// GIVEN
			saveToDB(
					TestData.createValidWashingMachine("serial4"),
					TestData.createValidWashingMachine("serial5")
			);

			// WHEN
			ResultActions resultActions = performRequest(
					Set.of("serial4", "serial5")
			);

			// THEN
			resultActions
					.andExpect(status().isOk())
					.andExpect(content().string(not(emptyString())));
		}

		private ResultActions performRequest(Set<String> request) throws Exception {
			return mockMvc.perform(
					post("/v1/washing-machines/many")
							.content(jackson.writeValueAsString(request))
							.contentType(MediaType.APPLICATION_JSON));
		}
	}
}
