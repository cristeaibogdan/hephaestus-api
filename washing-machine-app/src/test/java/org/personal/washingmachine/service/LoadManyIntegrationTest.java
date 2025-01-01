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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class LoadManyIntegrationTest extends BaseIntegrationTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@Autowired WashingMachineApplicationService underTest;
	@Autowired WashingMachineRepository repository;

	@BeforeEach
	void checkInitialDataInDB() {
		assertThat(repository.count()).isZero();
	}

	@Test
	void should_ReturnDTOs_When_SerialNumbersFound() {
		// GIVEN
		insertIntoDB(
				TestData.createWashingMachineWithSerialNumber("serial1"),
				TestData.createWashingMachineWithSerialNumber("serial2"),
				TestData.createWashingMachineWithSerialNumber("serial3")
		);

		// WHEN
		Map<String, GetWashingMachineFullResponse> actual = underTest.loadMany(List.of("serial1", "serial2"));

		// THEN
		assertThat(actual)
				.containsOnlyKeys(List.of("serial1", "serial2"));

		assertThat(actual.values())
				.extracting(wm -> wm.serialNumber())
				.containsExactlyInAnyOrderElementsOf(List.of("serial1", "serial2"));
	}

	@Test
	void should_ReturnDTOs_When_SerialNumbersContainNull() {
		// GIVEN
		insertIntoDB(
				TestData.createWashingMachineWithSerialNumber("serial1"),
				TestData.createWashingMachineWithSerialNumber("serial2"),
				TestData.createWashingMachineWithSerialNumber("serial3")
		);

		List<String> serialNumbers = new ArrayList<>();
		serialNumbers.add(null);
		serialNumbers.add("serial1");
		serialNumbers.add("serial2");

		List<String> expectedSerialNumbers = List.of("serial1", "serial2");

		// WHEN
		Map<String, GetWashingMachineFullResponse> actual = underTest.loadMany(serialNumbers);

		// THEN
		assertThat(actual)
				.containsOnlyKeys(expectedSerialNumbers);

		assertThat(actual.values())
				.extracting(wm -> wm.serialNumber())
				.containsExactlyInAnyOrderElementsOf(expectedSerialNumbers);
	}

	@Test
	void should_ReturnNullDTOs_When_SerialNumbersNotFound() {
		// GIVEN
		insertIntoDB(
				TestData.createWashingMachineWithSerialNumber("serial1"),
				TestData.createWashingMachineWithSerialNumber("serial2")
		);

		List<String> serialNumbers = List.of(
				"I don't exist",
				"serial1",
				"serial2",
				"Nothing"
		);

		Map<String, GetWashingMachineFullResponse> notFoundMap = new HashMap<>();
		notFoundMap.put("I don't exist", null);
		notFoundMap.put("Nothing", null);

		// WHEN
		Map<String, GetWashingMachineFullResponse> actual = underTest.loadMany(serialNumbers);

		// THEN
		assertThat(actual) //TODO: consider comparing with an actual Map<String, GetWashingMachineFullResponse>
//				.containsExactlyInAnyOrderEntriesOf(notFoundMap);
				.containsOnlyKeys(serialNumbers)
				.containsAllEntriesOf(notFoundMap);
	}

	private void insertIntoDB(WashingMachine... washingMachines) {
		repository.saveAll(List.of(washingMachines));
	}

	@Nested
	class MvcTest {

		@Test
		void should_ThrowCustomException_When_ListIsEmpty() throws Exception {
			// GIVEN
			// WHEN
			ResultActions resultActions = performRequest(List.of());

			// THEN
			resultActions
					.andExpect(status().isBadRequest())
					.andExpect(content().string(not(containsString("Internal Translation Error"))));
		}

		@Test
		void should_ThrowCustomException_When_SerialNumbersNotFound() throws Exception {
			// GIVEN
			// WHEN
			ResultActions resultActions = performRequest(
					List.of("I don't exist", "You won't find me")
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
			insertIntoDB(
					TestData.createWashingMachineWithSerialNumber("serial4"),
					TestData.createWashingMachineWithSerialNumber("serial5")
			);

			// WHEN
			ResultActions resultActions = performRequest(
					List.of("serial4", "serial5")
			);

			// THEN
			resultActions
					.andExpect(status().isOk())
					.andExpect(content().string(not(emptyString())));
		}

		private ResultActions performRequest(List<String> request) throws Exception {
			return mockMvc.perform(
					post("/api/v1/washing-machines/many")
							.content(jackson.writeValueAsString(request))
							.contentType(MediaType.APPLICATION_JSON));
		}
	}
}
