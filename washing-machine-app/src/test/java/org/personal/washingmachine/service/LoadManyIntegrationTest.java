package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.personal.washingmachine.BaseIntegrationTest;
import org.personal.washingmachine.TestData;
import org.personal.washingmachine.dto.GetWashingMachineFullResponse;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;
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
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional // Ensure Hibernate sessions are properly managed in your testing environment. Avoids "could not initialize proxy" Exception. Specific to tests only.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoadManyIntegrationTest extends BaseIntegrationTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@Autowired WashingMachineApplicationService washingMachineApplicationService;
	@Autowired WashingMachineRepository washingMachineRepository;

	@BeforeAll
	void loadDataInDB() {
		List<WashingMachine> washingMachines = List.of(
				new WashingMachine("Washing Machine", "Gorenje", DamageType.IN_USE, ReturnType.COMMERCIAL, IdentificationMode.DATA_MATRIX, "serial1", "modelA", "TypeZ", Recommendation.OUTLET, TestData.washingMachineDetail()),
				new WashingMachine("Washing Machine", "Gorenje", DamageType.IN_USE, ReturnType.COMMERCIAL, IdentificationMode.DATA_MATRIX, "serial2", "modelA", "TypeZ", Recommendation.OUTLET, TestData.washingMachineDetail()),
				new WashingMachine("Washing Machine", "Gorenje", DamageType.IN_USE, ReturnType.SERVICE, IdentificationMode.DATA_MATRIX, "serial3", "modelB", "TypeZ", Recommendation.REPACKAGE, TestData.washingMachineDetail()),
				new WashingMachine("Washing Machine", "Gorenje", DamageType.IN_USE, ReturnType.SERVICE, IdentificationMode.QR_CODE, "serial4", "modelB", "TypeX", Recommendation.REPAIR, TestData.washingMachineDetail()),
				new WashingMachine("Washing Machine", "Gorenje", DamageType.IN_USE, ReturnType.SERVICE, IdentificationMode.QR_CODE, "serial5", "modelC", "TypeX", Recommendation.REPAIR, TestData.washingMachineDetail())
		);

		washingMachineRepository.saveAll(washingMachines);
	}

	@AfterAll
	void cleanUpDB() {
		washingMachineRepository.deleteAll();
	}

	@Test
	void should_Count5ElementsInDB() {
		assertThat(washingMachineRepository.count()).isEqualTo(5);
	}

	@Test
	void should_ReturnWashingMachines_When_ProvidedValidSerialNumbers() {
		// GIVEN
		List<String> serialNumbers = List.of("serial1", "serial2", "serial3");

		// WHEN
		Map<String, GetWashingMachineFullResponse> actual = washingMachineApplicationService.loadMany(serialNumbers);

		// THEN
		assertThat(actual)
				.isNotEmpty()
				.containsOnlyKeys(serialNumbers)
				.extractingFromEntries(entry -> entry.getValue().serialNumber())
				.containsExactlyInAnyOrderElementsOf(serialNumbers);
	}

	@Test
	void should_ReturnWashingMachines_When_SerialNumbersContainNull() {
		// GIVEN
		List<String> serialNumbers = new ArrayList<>();
		serialNumbers.add(null);
		serialNumbers.add("serial1");
		serialNumbers.add("serial2");

		List<String> expectedSerialNumbers = List.of("serial1", "serial2");

		// WHEN
		Map<String, GetWashingMachineFullResponse> actual = washingMachineApplicationService.loadMany(serialNumbers);

		// THEN
		assertThat(actual)
				.isNotEmpty()
				.containsOnlyKeys(expectedSerialNumbers)
				.extractingFromEntries(entry -> entry.getValue().serialNumber())
				.containsExactlyInAnyOrderElementsOf(expectedSerialNumbers);
	}

	@Test
	void should_ReturnNullWashingMachines_When_SerialNumbersNotFound() {
		// GIVEN
		List<String> serialNumbers = List.of(
				"I don't exist",
				"serial1",
				"serial2",
				"serial3",
				"serial4",
				"Can't find me",
				"Nothing"
		);

		Map<String, GetWashingMachineFullResponse> notFoundMap = new HashMap<>();
		notFoundMap.put("I don't exist", null);
		notFoundMap.put("Can't find me", null);
		notFoundMap.put("Nothing", null);

		// WHEN
		Map<String, GetWashingMachineFullResponse> actual = washingMachineApplicationService.loadMany(serialNumbers);

		// THEN
		assertThat(actual)
				.isNotEmpty()
				.containsOnlyKeys(serialNumbers)
				.containsAllEntriesOf(notFoundMap);
	}

	@Nested
	class MvcTest {

		@Test
		void should_ThrowCustomException_When_ListIsEmpty() throws Exception {
			// GIVEN
			List<String> request = List.of();

			// WHEN
			ResultActions resultActions = performRequest(request);

			// THEN
			resultActions
					.andExpect(status().isBadRequest())
					.andExpect(content().string(not(containsString("Internal Translation Error"))));
		}

		@Test
		void should_ThrowCustomException_When_SerialNumbersNotFound() throws Exception {
			// GIVEN
			List<String> request = List.of("I don't exist", "Something", "You won't find me");

			// WHEN
			ResultActions resultActions = performRequest(request);

			// THEN
			resultActions
					.andExpect(status().isNotFound())
					.andExpect(content().string(containsString("I don't exist")))
					.andExpect(content().string(containsString("Something")))
					.andExpect(content().string(containsString("You won't find me")));
		}

		private ResultActions performRequest(List<String> request) throws Exception {
			return mockMvc.perform(
					post("/api/v1/washing-machines/many")
							.content(jackson.writeValueAsString(request))
							.contentType(MediaType.APPLICATION_JSON));
		}
	}
}
