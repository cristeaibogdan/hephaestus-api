package org.personal.washingmachine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.personal.washingmachine.BaseIntegrationTest;
import org.personal.washingmachine.entity.WashingMachine;
import org.personal.washingmachine.enums.DamageType;
import org.personal.washingmachine.enums.IdentificationMode;
import org.personal.washingmachine.enums.Recommendation;
import org.personal.washingmachine.enums.ReturnType;
import org.personal.washingmachine.repository.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetRecommendationIntegrationTest extends BaseIntegrationTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper jackson;

	@Autowired WashingMachineApplicationService underTest;
	@Autowired WashingMachineRepository washingMachineRepository;

	@BeforeAll
	void loadDataInDB() {
		washingMachineRepository.save(
				new WashingMachine(
						"Washing Machine",
						"Gorenje",
						DamageType.IN_USE,
						ReturnType.COMMERCIAL,
						IdentificationMode.DATA_MATRIX,
						"I exist in DB",
						"modelA",
						"TypeZ",
						Recommendation.OUTLET,
						null
				)
		);
	}

	@AfterAll
	void cleanUpDB() {
		washingMachineRepository.deleteAll();
	}

	@BeforeEach
	void checkInitialDataInDB() {
		assertThat(washingMachineRepository.count()).isEqualTo(1);
	}

	@Nested
	class MvcTest {

		@Test
		void should_ReturnExpectedContent_When_SerialNumberExists() throws Exception {
			// GIVEN
			// WHEN
			ResultActions resultActions = performRequest("I exist in DB");

			// THEN
			resultActions
					.andExpect(status().isOk())
					.andExpect(content().string(jackson.writeValueAsString(Recommendation.OUTLET)));
		}

		@Test
		void should_ThrowCustomException_When_SerialNumberNotFound() throws Exception {
			// GIVEN
			// WHEN
			ResultActions resultActions = performRequest("I don't exist");

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

}
